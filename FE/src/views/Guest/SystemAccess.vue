<template>
  <div class="systemaccess">
    
    <div class="content-right">
      <LoginPage
        v-show="LoginSate == 1"
        @change-Page="changePage"
        @show-toast="showToast"
      />
      <RegisterPage
        v-show="LoginSate == 2"
        @change-Page="changePage"
        @show-toast="showToast"
      />
      <ForgotPasswordPage
        v-show="LoginSate == 3"
        @change-Page="changePage"
        @show-toast="showToast"
      />
    </div>
    <div class="content-left">
      <div class="content-img">
        <img class="login-img" src="@\assets\photos\1.jpg" />
      </div>
    </div>
  </div>
</template>
<script>
import LoginPage from "./LoginPage.vue";
import RegisterPage from "./RegisterPage.vue";
import ForgotPasswordPage from "./ForgotPasswordPage.vue";
import { mapMutations } from "vuex";
import {
  TOAST_SHOW_MUTATION,

} from "@/stores/modules/storeconstants";
export default {
  name: "SystemAccess",
  components: {
    LoginPage,
    RegisterPage,
    ForgotPasswordPage,
  },
  computed: {
  },
  methods: {
    ...mapMutations({
      Toast: TOAST_SHOW_MUTATION,
      
    }),
    changePage(page) {
      console.log(page);
      this.LoginSate = page;
    },

    /**
    Hàm hiện thị thông báo
     */
    showToast(Toastcssicon, Toastcss, ToastMess_color, ToastMess) {
      this.ToastInfor = {
        show: true,
        icon: Toastcssicon,
        css: Toastcss,
        color: ToastMess_color,
        message: ToastMess,
      };
      this.Toast(this.ToastInfor);
    },
  },
  data() {
    return {
      LoginSate: 1,
      ToastInfor: {
        //trang thái thông báo
        show: true,
        //icon thông báo
        icon: "",
        //css thông báo
        css: "",
        //màu nội dung thông báo
        color: "",
        //nội dung thông báo
        message: "",
      },
    };
  },
};
</script>

<style scoped>
.systemaccess {
  order: 3;
  width: 97%;
  height: calc(100vh);
  float: right;
  display: flex;
  background-color: #ecf0f1;
  position: relative;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}

.content-right {
  width: calc(100% - 50%);
  height: 95%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  margin-left: 6%;
  padding: 2%;
}
.content-left {
  width: calc(100% - 60%);
  height: 100%;
}
.content-img {
  width: 100%;
  height: 100%;
}
.login-img {
  margin-left: 27.4%;
  width: 90.3%;
  height: 90%;
}
.content-right.top {
  width: 100%;
  height: 10%;
}
.top-label {
  width: 45%;
  height: 100%;
}
.system-label {
  font-size: 30px;
  font-weight: bold;
}
.content-right.center {
  width: 100%;
  height: 45%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
}
.center-label {
  width: 100%;
  height: 10%;
  text-align: left;
}

@media (max-width: 1200px) {
  .content-left {
    display: none;
  }
  .content-right {
    width: 100%;
    margin-left: 0px;
    padding: 5%;
  }
  .systemaccess {
    width: 100%;
  }
}
</style>
