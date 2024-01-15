<template>
  <div class="card-chat">
    <div class="chat-header">
      <div class="chat-header_left">Chat</div>
      <div @click="closeChat" class="chat-header_right">
        <i class="ti ti-x"></i>
      </div>
    </div>
    <div class="" v-if="isAuthenticated">
      <div class="chat-window" ref="chatWindow">
        <ul class="message-list">
          <li
            v-for="message in messages"
            :key="message"
            :class="
              message.isYou === true ? 'message-item_you' : 'message-item_their'
            "
          >
            {{ message.content }}
          </li>
        </ul>
      </div>
      <div class="chat-input">
        <input
          type="text"
          class="message-input"
          maxlength="70"
          placeholder="Nhập tin nhắn của bạn"
          v-model="newMessage"
        />
        <v-btn
          class="send-button"
          :disabled="newMessage.trim() == '' ? true : false"
          @click="sendMessage"
          icon="ti ti-send"
        >
        </v-btn>
      </div>
    </div>
    <div class="chat-none_auth" v-else>
      <span>Vui lòng đăng nhập vào hệ thống trước để sử dụng tính năng</span>
      <v-btn class="btn" color="#35bf22" append-icon="ti ti-arrow-narrow-right"
        ><router-link class="text-decoration-none text-none" to="/login"
          >Đăng nhập</router-link
        ></v-btn
      >
    </div>
  </div>
</template>

<script>
import {
  IS_AUTHENTICATE_GETTER,
  SET_CHAT_ISOPENED,
  GET_CHAT_ISOPENED,
  SEND_NOTIFICATION_MUTATION,
} from "@/stores/modules/storeconstants";
import { mapGetters, mapMutations } from "vuex";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import notification from "@/resouce/notification";
export default {
  created() {
    (async () => {
      if (this.isAuthenticated) {
        this.receiverId = this.getChat.receivedId;
        await this.loadProfile();
        await this.loadOldMessage();
        this.connect();
        this.scrollToBottom();
      }
    })();
  },
  methods: {
    ...mapMutations({
      showChat: SET_CHAT_ISOPENED,
      sendNotification: SEND_NOTIFICATION_MUTATION,
    }),
    scrollToBottom() {
      try {
        const chatWindow = this.$refs.chatWindow;
        chatWindow.scrollTop = chatWindow.scrollHeight;
      } catch (error) {
        console.log(error);
      }
    },
    async loadOldMessage() {
      try {
        await fetch(
        `${apiConfig}/api/chats/load/${this.receiverId}`,
        await fetchOptions("GET")
      )
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          console.log(data);
          data.map((message) => {
            if (message.senderId === this.senderId) {
              this.messages.push({
                content: message.content,
                isYou: true,
              });
            } else {
              this.messages.push({
                content: message.content,
                isYou: false,
              });
            }
          });
        })
        .catch((res) => {
         console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async loadProfile() {
      try {
        await fetch(`${apiConfig}/users/my-profile/`, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.senderId = data.id;
          this.role = data.role.position;
        })
        .catch((res) => {
          console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    CreateChannel(role, receiverId, yourId) {
      if (role === "USER") {
        return `${yourId}${receiverId}`;
      } else {
        return `${receiverId}${yourId}`;
      }
    },
    closeChat() {
      this.showChat({
        show: false,
        receivedId: "",
      });
    },
    beforeDestroy() {
      if (this.stompClient !== null) {
        this.stompClient.disconnect();
      }
    },
    connect() {
      const headers = {
        Authorization:
          "Bearer " +
          JSON.parse(localStorage.getItem("userData"))["access_token"],
      };

      const socket = new SockJS(`${apiConfig}/chat`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(headers, this.onConnected, this.onError);
    },
    onConnected() {
      this.stompClient.subscribe(
        `/user/queue/messages/${this.CreateChannel(
          this.role,
          this.receiverId,
          this.senderId
        )}`,
        this.onMessageReceived
      );
      console.log("Connected to WebSocket server.");
    },
    onMessageReceived(payload) {
      const message = JSON.parse(payload.body);
      if (this.isYou) {
        this.messages.push({
          content: message.content,
          isYou: true,
        });
      } else {
        this.messages.push({
          content: message.content,
          isYou: false,
        });
      }
      this.isYou = false;
      // Sau khi cập nhật DOM, cuộn xuống cuối cùng
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    onError(error) {
      console.error("Error during WebSocket connection:", error);
      // Add your notification logic here
    },
    async sendMessage() {
      if (this.stompClient && this.stompClient.connected) {
        if (this.newMessage.trim() !== "") {
          const chatDTO = {
            content: this.newMessage,
            receiverId: this.receiverId,
          };
          this.stompClient.send(
            `/user/queue/messages/${this.CreateChannel(
              this.role,
              this.receiverId,
              this.senderId
            )}`,
            {},
            JSON.stringify(chatDTO)
          );
          //chạy api để thêm vào database và lấy ra response
          this.sendNotification({
            content: notification.MESSAGE,
            receiverId: this.receiverId,
          });
          this.isYou = true;
          fetch(
            `${apiConfig}/api/chats/send`,
            await fetchOptions("POST", chatDTO)
          )
            .then((response) => response.json().catch(() => response))
            .then((response) => {
              console.log(response);
              ///Thêm thông báo phía trong này
            })
            .catch((error) => {
              console.log(error);
            });
          this.newMessage = "";
        } else {
          console.error("Không thể gửi tin nhắn");
          // Add your notification logic here
        }
      } else {
        console.error("WebSocket hết hạn.");
        // Add your notification logic here
      }
    },
  },
  data() {
    return {
      stompClient: null,
      messages: [],
      newMessage: "",
      isYou: false,
      senderId: "",
      role: "",
      receiverId: "",
    };
  },
  computed: {
    ...mapGetters("auth", {
      isAuthenticated: IS_AUTHENTICATE_GETTER,
    }),
    ...mapGetters({
      getChat: GET_CHAT_ISOPENED,
    }),
  },
};
</script>

<style scoped>
.card-chat {
  width: 260px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 999;
}
.chat-none_auth {
  background-color: #fff;
  height: 270px;
  padding: 20px;
}
.btn {
  margin-top: 50px;
}
.chat-header {
  background-color: #333;
  color: #fff;
  padding: 10px;
  font-size: 18px;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  display: flex;
  justify-content: space-between;
}
.chat-header_right {
  border-radius: 100%;
}
.chat-header_right:hover {
  cursor: pointer;
}
.chat-window {
  height: 220px;
  overflow-y: scroll;
}

.message-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 10px;
}
.message-item_you,
.message-item_their {
  text-align: start;
  align-self: flex-start;
  padding: 5px;
  border-radius: 15px;
  background-color: #4285f4;
  color: #fff;
  overflow-wrap: anywhere;
}
.message-item_their {
  align-self: flex-end;
  background-color: #e1e1e1;
  color: #1f1f1f;
}
.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ccc;
}

.message-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 5px;
  font-size: 14px;
}
</style>
