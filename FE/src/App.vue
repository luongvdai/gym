<template>
  <div class="container">
    <TheHeaderVue class="header" v-if="layout"></TheHeaderVue>
    <defaultLoading v-if="showLoading" />
    <router-view></router-view>
    <TheFooterNewVue v-if="layout"></TheFooterNewVue>
    <MToast
      v-if="displayToast.show"
      :text="displayToast.message"
      :text_color="displayToast.color"
      :classcss="displayToast.css"
      :classcssicon="displayToast.icon"
    />
    <ChooseBranchPopup v-if="hasBranch === 'NOT_YET'" />
    <ChatPopupVue
      class="chat-popup"
      v-if="showChat.show"
      :receivedId="showChat.receivedId"
    />
    <MPopupNotification v-if="isAuthenticated" />
    <GuestAdvicePopup
      v-if="isShowConsultant"
      @close-popup-click="closeGuestAdvicePopup"
    />
    <OtpPopupVue class="otp-popup" v-if="!isVerify" />
    <div
      class="send-question"
      v-if="layout"
      @click="isShowConsultant = !isShowConsultant"
    >
      <img src="@\assets\icons\send-question.png" alt="" />
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
import { defineAsyncComponent } from "vue";
import TheFooterNewVue from "@/components/TheFooter/TheFooterNew.vue";
import OtpPopupVue from "@/components/MPopup/OtpPopup.vue";
import TheHeaderVue from "@/components/TheHeader/TheHeader.vue";
import GuestAdvicePopup from "@/components/MPopup/GuestAdvicePopup.vue";
import ChooseBranchPopup from "./components/MPopup/ChooseBranchPopup.vue";
import {
  AUTO_LOGIN_ACTION,
  AUTO_NOTIFICATION_USER_ACTION,
  IS_AUTHENTICATE_GETTER,
  OTP_IS_VERIFY_GETTER,
  BRANCH_IS_VERIFY_GETTER,
  BRANCH_CHECK_ACTION,
} from "@/stores/modules/storeconstants";
import ChatPopupVue from "./components/MPopup/ChatPopup.vue";
const MToast = defineAsyncComponent(() =>
  import("@/components/MToast/MToast.vue")
);
const defaultLoading = defineAsyncComponent(() =>
  import("@/components/MLoading/defaulLoading.vue")
);
import MPopupNotification from "./components/MPopup/NotificationPopup.vue";
export default {
  name: "App",
  data() {
    return {
      isShowConsultant: false,
    };
  },
  components: {
    MToast,
    defaultLoading,
    TheHeaderVue,
    ChatPopupVue,
    MPopupNotification,
    OtpPopupVue,
    TheFooterNewVue,
    GuestAdvicePopup,
    ChooseBranchPopup,
  },
  computed: {
    ...mapGetters("auth", {
      isAuthenticated: IS_AUTHENTICATE_GETTER,
      isVerify: OTP_IS_VERIFY_GETTER,
      hasBranch: BRANCH_IS_VERIFY_GETTER,
    }),
    ...mapState({
      displayToast: (state) => state.displayToast,
      showLoading: (state) => state.showLoading,
      showChat: (state) => state.chatStatus,
    }),
    layout() {
      return !this.$route.path.startsWith("/admin");
    },
  },
  methods: {
    closeGuestAdvicePopup() {
      this.isShowConsultant = false;
    },
  },
  created() {
    this.$store.dispatch(`auth/${AUTO_LOGIN_ACTION}`);
    this.$store.dispatch(`${AUTO_NOTIFICATION_USER_ACTION}`);
    this.$store.dispatch(`auth/${BRANCH_CHECK_ACTION}`);
  },
};
</script>

<style>
:root {
  --loading-icon: url("@/assets/icons/loading.svg");
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.container {
  min-height: 100vh;
  width: fit-content;
  scroll-behavior: smooth;
}
.container {
  font-size: 14px;
  width: 100%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  background-color: #ecf0f1;
}
* {
  margin: 0;
  padding: 0;
}
.chat-popup {
  position: fixed;
  bottom: 15px;
  right: 85px;
}
.header {
  z-index: 1;
}
.send-question {
  position: fixed;
  right: 15px;
  bottom: 75px;
  border-radius: 50%;
  box-shadow: 1px 1px 11px 1px #a59a9a;
  background-color: white;
  height: 48px;
  width: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.send-question:hover {
  background-color: rgb(235, 234, 234);
}
.send-question:hover img {
  transform: scale(1.2);
}
.send-question img {
  height: 35px;
  width: auto;
  transition: transform 0.3s ease;
}
</style>
