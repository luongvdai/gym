<template>
  <div class="notification-dashbroad">
    <v-card>
      <v-layout>
        <v-navigation-drawer v-model="drawer" temporary>
          <v-card>
            <v-toolbar :color="color" dark>
              <v-toolbar-title>Thông báo</v-toolbar-title>
              <v-btn icon @click="drawer = !drawer">
                <i class="ti ti-x"></i>
              </v-btn> </v-toolbar
          ></v-card>
          <v-sheet
            lines="two"
            class="mx-auto sidebar-header-list"
            max-width="600"
          >
            <v-slide-group show-arrows>
              <v-slide-group-item v-for="n in lstSelect" :key="n">
                <v-btn
                  class="ma-2 text-none"
                  :class="btnSelected == n.option ? 'btn-active' : ''"
                  rounded
                  @click="selectOption(n.option)"
                >
                  {{ n.name }}
                </v-btn>
              </v-slide-group-item>
            </v-slide-group>
          </v-sheet>
          <v-list lines="three" v-if="showLoading">
            <v-list-item v-for="index in 6" :key="index" class="text-start">
              <v-skeleton-loader type="paragraph"></v-skeleton-loader>
            </v-list-item>
          </v-list>
          <v-list lines="three" v-else>
            <v-list-item
              v-for="item in allNotifications"
              :key="item.senderId"
              class="text-start card-notification"
              @click="ReadNotification(item)"
            >
              <v-list-item-title>{{
                item.senderId === item.receiverId
                  ? "Hệ thống"
                  : item.sender.fullName
              }}</v-list-item-title>

              <v-list-item-subtitle>
                {{ item.content }}
              </v-list-item-subtitle>
              <span v-if="!item.isRead" class="notification-status"></span>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
        <v-main class="notification-screen">
          <div class="btn-notification">
            <v-btn :color="color" icon @click="drawer = !drawer" title="Thông báo">
              <i
                class="ti ti-bell"
                :class="
                  allNotifications.find((noti) => noti.isRead == false)
                    ? 'notification-ring'
                    : ''
                "
              ></i>
            </v-btn>
          </div>
        </v-main>
      </v-layout>
    </v-card>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import {
  GET_NOTIFICATION,
  SET_CHAT_ISOPENED,
  SET_NOTIFICATION_STATUS,
  SET_NOTIFICATION_LOAD,
} from "@/stores/modules/storeconstants";
import notification from "@/resouce/notification";

export default {
  data() {
    return {
      color: "",
      drawer: null,
      btnSelected: 1,
      lstSelect: [
        {
          option: 1,
          name: "Tất cả",
        },
        {
          option: 2,
          name: "Chưa đọc",
        },
      ],
      showLoading: false,
    };
  },
  methods: {
    async selectOption(option) {
      if (this.btnSelected != option) {
        this.btnSelected = option;
        this.loadNotification({ option: this.btnSelected });
      }
    },
    async ReadNotification(item) {
      if (item.content === notification.MESSAGE) {
        await this.showChat({
          show: false,
        });
        await this.showChat({
          show: true,
          receivedId: item.senderId,
        });
      }

      await this.changeStatus(item);
    },
    ...mapMutations({
      showChat: SET_CHAT_ISOPENED,
      changeStatus: SET_NOTIFICATION_STATUS,
      loadNotification: SET_NOTIFICATION_LOAD,
    }),
  },
  computed: {
    ...mapGetters({
      allNotifications: GET_NOTIFICATION,
    }),
  },
};
</script>

<style scoped>
.notification-dashbroad {
  z-index: 9999;
}
.notification-screen {
  position: fixed;
  bottom: 15px;
  right: 15px;
}
.ti-bell {
  font-size: 1.5em;
}
.notification-ring {
  -webkit-animation: ring 4s 0.7s ease-in-out infinite;
  -webkit-transform-origin: 50% 4px;
  -moz-animation: ring 4s 0.7s ease-in-out infinite;
  -moz-transform-origin: 50% 4px;
  animation: ring 4s 0.7s ease-in-out infinite;
  transform-origin: 50% 4px;
}
.notification-ring::after {
  content: "";
  position: absolute;
  background-color: red;
  height: 8px;
  width: 8px;
  border-radius: 100%;
  top: 20%;
  left: 55%;
}

.notification-status::before,
.notification-status::after {
  content: "";
  position: absolute;
  right: 15px;
  top: 30%;
  width: 10px;
  height: 10px;
  background-color: #1876f2;
  border-radius: 50%;
}
.notification-status::before {
  animation: ping 1.5s ease infinite;
  opacity: 0.5;
}
@keyframes ping {
  0% {
    transform: scale(1);
    opacity: 0.7;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}
.card-notification {
  margin-bottom: 5px;
}
.card-notification:hover {
  background-color: rgba(0, 0, 0, 0.1);
  cursor: pointer;
}
.btn-active {
  background-color: #53a336;
  color: #fff;
}
@-webkit-keyframes ring {
  0% {
    -webkit-transform: rotateZ(0);
  }
  1% {
    -webkit-transform: rotateZ(30deg);
  }
  3% {
    -webkit-transform: rotateZ(-28deg);
  }
  5% {
    -webkit-transform: rotateZ(34deg);
  }
  7% {
    -webkit-transform: rotateZ(-32deg);
  }
  9% {
    -webkit-transform: rotateZ(30deg);
  }
  11% {
    -webkit-transform: rotateZ(-28deg);
  }
  13% {
    -webkit-transform: rotateZ(26deg);
  }
  15% {
    -webkit-transform: rotateZ(-24deg);
  }
  17% {
    -webkit-transform: rotateZ(22deg);
  }
  19% {
    -webkit-transform: rotateZ(-20deg);
  }
  21% {
    -webkit-transform: rotateZ(18deg);
  }
  23% {
    -webkit-transform: rotateZ(-16deg);
  }
  25% {
    -webkit-transform: rotateZ(14deg);
  }
  27% {
    -webkit-transform: rotateZ(-12deg);
  }
  29% {
    -webkit-transform: rotateZ(10deg);
  }
  31% {
    -webkit-transform: rotateZ(-8deg);
  }
  33% {
    -webkit-transform: rotateZ(6deg);
  }
  35% {
    -webkit-transform: rotateZ(-4deg);
  }
  37% {
    -webkit-transform: rotateZ(2deg);
  }
  39% {
    -webkit-transform: rotateZ(-1deg);
  }
  41% {
    -webkit-transform: rotateZ(1deg);
  }

  43% {
    -webkit-transform: rotateZ(0);
  }
  100% {
    -webkit-transform: rotateZ(0);
  }
}

@-moz-keyframes ring {
  0% {
    -moz-transform: rotate(0);
  }
  1% {
    -moz-transform: rotate(30deg);
  }
  3% {
    -moz-transform: rotate(-28deg);
  }
  5% {
    -moz-transform: rotate(34deg);
  }
  7% {
    -moz-transform: rotate(-32deg);
  }
  9% {
    -moz-transform: rotate(30deg);
  }
  11% {
    -moz-transform: rotate(-28deg);
  }
  13% {
    -moz-transform: rotate(26deg);
  }
  15% {
    -moz-transform: rotate(-24deg);
  }
  17% {
    -moz-transform: rotate(22deg);
  }
  19% {
    -moz-transform: rotate(-20deg);
  }
  21% {
    -moz-transform: rotate(18deg);
  }
  23% {
    -moz-transform: rotate(-16deg);
  }
  25% {
    -moz-transform: rotate(14deg);
  }
  27% {
    -moz-transform: rotate(-12deg);
  }
  29% {
    -moz-transform: rotate(10deg);
  }
  31% {
    -moz-transform: rotate(-8deg);
  }
  33% {
    -moz-transform: rotate(6deg);
  }
  35% {
    -moz-transform: rotate(-4deg);
  }
  37% {
    -moz-transform: rotate(2deg);
  }
  39% {
    -moz-transform: rotate(-1deg);
  }
  41% {
    -moz-transform: rotate(1deg);
  }

  43% {
    -moz-transform: rotate(0);
  }
  100% {
    -moz-transform: rotate(0);
  }
}

@keyframes ring {
  0% {
    transform: rotate(0);
  }
  1% {
    transform: rotate(30deg);
  }
  3% {
    transform: rotate(-28deg);
  }
  5% {
    transform: rotate(34deg);
  }
  7% {
    transform: rotate(-32deg);
  }
  9% {
    transform: rotate(30deg);
  }
  11% {
    transform: rotate(-28deg);
  }
  13% {
    transform: rotate(26deg);
  }
  15% {
    transform: rotate(-24deg);
  }
  17% {
    transform: rotate(22deg);
  }
  19% {
    transform: rotate(-20deg);
  }
  21% {
    transform: rotate(18deg);
  }
  23% {
    transform: rotate(-16deg);
  }
  25% {
    transform: rotate(14deg);
  }
  27% {
    transform: rotate(-12deg);
  }
  29% {
    transform: rotate(10deg);
  }
  31% {
    transform: rotate(-8deg);
  }
  33% {
    transform: rotate(6deg);
  }
  35% {
    transform: rotate(-4deg);
  }
  37% {
    transform: rotate(2deg);
  }
  39% {
    transform: rotate(-1deg);
  }
  41% {
    transform: rotate(1deg);
  }

  43% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(0);
  }
}
</style>
