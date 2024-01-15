<template>
  <div class="OTP-popup">
    <v-card
      class="OTP-card py-12 px-8 text-center mx-auto ma-4"
      max-width="420"
      width="100%"
    >
      <h3 class="text-h6 mb-2">
        Một mã <strong>OTP</strong> đã được gửi tới tài khoản email của bạn
      </h3>
      <div>
        Vui lòng nhập mã OTP một lần để xác minh tài khoản của bạn trước khi
        đăng nhập vào hệ thống
      </div>
      <v-otp-input
        v-model="otp"
        :disabled="validating"
        :error="error"
      ></v-otp-input>
      <div class="text-error" v-if="countError == 3">
        Một mã <strong>OTP</strong> khác đã được gửi vào Email của bạn
      </div>
      <v-btn
        :loading="validating"
        border
        class="mt-6 text-none bg-surface-variant"
        rounded
        variant="plain"
        text="Xác thực"
        height="40"
        width="135"
        @click="Verify()"
      ></v-btn>
      <v-btn class="otp-close" variant="fill" icon @click="closePopup()">
        <i class="ti ti-x"></i>
      </v-btn>
    </v-card>
  </div>
</template>

<script>
import { mapMutations, mapGetters, mapActions } from "vuex";
import {
  OTP_VERIFY_SHOW_MUTATION,
  GET_USER_TOKEN_GETTER,
  LOGIN_ACTION,
  LOGOUT_ACTION,
} from "@/stores/modules/storeconstants";
import apiConfig from "@/api/config/apiConfig";
export default {
  data() {
    return {
      otp: "",
      validating: false,
      error: false,
      countError: 0,
    };
  },
  created() {
    (async () => {
      await this.sendOTP();
    })();
  },
  computed: {
    ...mapGetters("auth", {
      access_token: GET_USER_TOKEN_GETTER,
    }),
  },
  methods: {
    ...mapActions("auth", {
      login_action: LOGIN_ACTION,
      logout: LOGOUT_ACTION,
    }),
    ...mapMutations("auth", {
      isVerify: OTP_VERIFY_SHOW_MUTATION,
    }),
    closePopup() {
      this.logout();
      this.isVerify(true);
    },
    async sendOTP() {
      await fetch(`${apiConfig}/users/get-otp`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${this.access_token}`,
        },
      })
        .then((response) => response.json().catch(() => response))
        .then((response) => response)
        .catch((error) => {
          console.log(error);
        });
    },
    async Verify() {
      this.validating = true;
      if (this.countError == 2) {
        this.sendOTP();
        setTimeout(() => {
          this.countError = 0;
        }, 5000);
      }
      try {
        await fetch(`${apiConfig}/users/validate-otp?otp=${this.otp}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${this.access_token}`,
          },
        })
          .then((response) => response.json().catch(() => response))
          .then(async (response) => {
            if (response.status && response.status == 200) {
              //call api refresh token
              await fetch(`${apiConfig}/api/auth/refresh-token`, {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${this.access_token}`,
                },
              })
                .then((response) => response.json().catch(() => response))
                .then((response) => {
                  this.login_action(response);
                })
                .catch((error) => {
                  console.log(error);
                });
            } else {
              this.otp = "";
              this.error = true;
              this.countError++;
            }
          })
          .catch((error) => {
            console.log(error);
            this.otp = "";
            this.error = true;
          });
      } catch (error) {
        this.otp = "";
        console.log(error);
      }
      this.validating = false;
    },
  },
};
</script>

<style scoped>
.OTP-popup {
  width: 100%;
  min-height: 100vh;
  background: rgba(0, 0, 0, 0.1);
  z-index: 9999;
  position: absolute;
}
.OTP-card {
  position: absolute;
  top: 30%;
  left: 15%;
}
.otp-close {
  position: absolute;
  top: 0%;
  right: 0%;
}
</style>
