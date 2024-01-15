import { createStore } from "vuex";
import auth from "./auth/index";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
  SET_BRANCH_SELECTED,
  GET_BRANCH_SELECTED,
  GET_BRANCH_HOME_SELECTED,
  SET_BRANCH_HOME_SELECTED,
  SET_ORDERSTATUS,
  GET_ORDERSTATUS,
  SET_CHAT_ISOPENED,
  AUTO_NOTIFICATION_USER_ACTION,
  GET_NOTIFICATION,
  SET_NOTIFICATION_RECEIVED,
  GET_CHAT_ISOPENED,
  SET_NOTIFICATION_STATUS,
  SET_STOMP_DISCONNECT,
  SEND_NOTIFICATION_MUTATION,
  SET_NOTIFICATION_LOAD,
} from "./storeconstants";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import notification from "@/resouce/notification";

const store = createStore({
  modules: {
    auth,
  },
  state() {
    return {
      showLoading: false,
      displayToast: {
        show: false,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      branchSelected: "",
      branchHomeSelected: {},
      orderSuccessPageInitialized: false,
      chatStatus: {
        show: false,
        receivedId: "",
      },
      notificationMessage: [],
      lstUpdateNotification: [],
      stompClient: null,
    };
  },
  getters: {
    [GET_BRANCH_SELECTED]: (state) => {
      return state.branchSelected;
    },
    [GET_BRANCH_HOME_SELECTED]: (state) => {
      return state.branchHomeSelected;
    },
    [GET_NOTIFICATION]: (state) => {
      return state.notificationMessage;
    },
    [GET_ORDERSTATUS]: (state) => {
      return state.orderSuccessPageInitialized;
    },
    [GET_CHAT_ISOPENED]: (state) => {
      return state.chatStatus;
    },
  },
  mutations: {
    [LOADING_SPINNER_SHOW_MUTATION](state, payload) {
      state.showLoading = payload;
    },
    [TOAST_SHOW_MUTATION](state, payload) {
      state.displayToast.show = payload.show;
      state.displayToast.icon = payload.icon;
      state.displayToast.css = payload.css;
      state.displayToast.color = payload.color;
      state.displayToast.message = payload.message;
      setTimeout(() => {
        state.displayToast.show = false;
      }, 4000);
    },
    [SET_BRANCH_SELECTED](state, payload) {
      state.branchSelected = payload.id;
    },
    [SET_BRANCH_HOME_SELECTED](state, payload) {
      state.branchHomeSelected = payload.branch;
    },
    [SET_ORDERSTATUS](state, value) {
      state.orderSuccessPageInitialized = value;
    },
    [SET_CHAT_ISOPENED](state, payload) {
      state.chatStatus.show = payload.show;
      state.chatStatus.receivedId = payload.receivedId;
    },
    async [SET_NOTIFICATION_RECEIVED](state, payload) {
      const existingNotificationIndex = state.notificationMessage.findIndex(
        (item) =>
          item.senderId === payload.senderId &&
          item.content === notification.MESSAGE
      );

      if (existingNotificationIndex === -1) {
        // Nếu thông báo chưa tồn tại, thêm mới vào mảng
        state.notificationMessage.unshift(payload);
      } else {
        // Nếu thông báo đã tồn tại, cập nhật và đưa lên đầu tiên
        state.notificationMessage.splice(existingNotificationIndex, 1);
        state.notificationMessage.unshift(payload);
      }
      state.lstUpdateNotification.push({
        notificationId: payload.id,
        senderId: payload.senderId,
      });
    },
    async [SET_NOTIFICATION_STATUS](state, payload) {
      console.log(payload.isRead);
      if (!payload.isRead) {
        if (payload.content === notification.MESSAGE) {
          state.lstUpdateNotification.forEach(async (notification) => {
            if (notification.senderId === payload.senderId) {
              await fetch(
                `${apiConfig}/notification/${notification.notificationId}`,
                await fetchOptions("PUT", {
                  isRead: true,
                })
              )
                .then((data) => data.json().catch(() => data))
                .then((response) => {
                  console.log(response);
                  const notification = state.notificationMessage.find(
                    (item) => item.id == response.id
                  );
                  notification.isRead = true;
                })
                .catch((res) => {
                  console.log(res);
                });
            }
          });
        } else {
          await fetch(
            `${apiConfig}/notification/${payload.id}`,
            await fetchOptions("PUT", {
              isRead: true,
            })
          )
            .then((data) => data.json().catch(() => data))
            .then((response) => {
              const notification = state.notificationMessage.find(
                (item) => item.id == response.id
              );
              notification.isRead = true;
            })
            .catch((res) => {
              console.log(res);
            });
        }
      }
    },
    [SET_STOMP_DISCONNECT](state) {
      // if (state.stompClient !== null) {
      //   state.stompClient.disconnect();
      // }
      console.log(state);
    },
    async [SEND_NOTIFICATION_MUTATION](state, payload) {
      await fetch(
        `${apiConfig}/notification`,
        await fetchOptions("POST", {
          content: payload.content,
          isRead: false,
          receiverId: payload.receiverId,
        })
      )
        .then((data) => data.json().catch(() => data))
        .then((response) => {
          state.stompClient.send(
            `/user/queue/messages/notification/${payload.receiverId}`,
            {},
            JSON.stringify({
              response,
            })
          );
        })
        .catch((res) => {
          console.log(res);
        });
    },
    async [SET_NOTIFICATION_LOAD](state, payload) {
      let filter = "";
      if (payload.option == 2) {
        filter = "isRead=false";
      }
      await fetch(
        `${apiConfig}/notification?${filter}`,
        await fetchOptions("GET")
      )
        .then((data) => data.json().catch(() => data))
        .then((response) => {
          // Lọc các phần tử không có id trùng lặp
          state.notificationMessage = response.content.reduce((acc, item) => {
            if (item.content === notification.MESSAGE) {
              state.lstUpdateNotification.push({
                notificationId: item.id,
                senderId: item.senderId,
              });
              // Kiểm tra xem id và content đã xuất hiện hay chưa
              let existingIndex = acc.findIndex(
                (existingItem) =>
                  existingItem.senderId === item.senderId &&
                  existingItem.content === notification.MESSAGE
              );
              if (existingIndex !== -1) {
                // Nếu id đã xuất hiện, và status là false, thì thay thế phần tử trước đó
                if (item.isRead === false) {
                  acc[existingIndex] = item;
                }
              } else {
                // Nếu id chưa xuất hiện, thêm phần tử vào mảng
                acc.push(item);
              }
            } else {
              acc.push(item);
            }
            return acc;
          }, []);
        })
        .catch((res) => {
          console.log(res);
        });
    },
  },
  actions: {
    [AUTO_NOTIFICATION_USER_ACTION](context) {
      let isAuth = localStorage.getItem("userData");
      if (isAuth) {
        let auth = JSON.parse(isAuth)["access_token"];
        const headers = {
          Authorization: "Bearer " + auth,
        };
        const socket = new SockJS(`${apiConfig}/chat`);
        context.state.stompClient = Stomp.over(socket);
        context.state.stompClient.connect(
          headers,
          async function () {
            //lấy id để thêm vào ws
            fetch(`${apiConfig}/users/my-profile`, await fetchOptions("GET"))
              .then((data) => data.json().catch(() => data))
              .then((response) => {
                context.state.stompClient.subscribe(
                  `/user/queue/messages/notification/${response.id}`,
                  function (payload) {
                    const data = JSON.parse(payload.body);
                    console.log(data);
                    context.commit(SET_NOTIFICATION_RECEIVED, data.response);
                  }
                );
              })
              .catch((res) => {
                console.log(res);
              });
            //Load thông báo khi vừa login thành công
            context.commit(SET_NOTIFICATION_LOAD, { option: 1 });
            console.log("Connected to WebSocket server.");
          },
          function (error) {
            console.log(error);
          }
        );
      }
    },
  },
});
export default store;
