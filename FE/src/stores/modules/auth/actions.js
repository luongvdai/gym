import {
  LOGIN_WITH_GOOGLE_ACTION,
  DECODE_TOKEN,
  LOGOUT_ACTION,
  AUTO_LOGIN_ACTION,
  SET_USER_TOKEN_DATA_MUTATION,
  LOGIN_WITH_FACEBOOK_ACTION,
  LOGIN_ACTION,
  AUTO_LOGOUT_ACTION,
  SET_AUTO_LOGOUT_MUTATION,
  SET_EXPIRATION_TIME_ACTION,
  TOAST_SHOW_MUTATION,
  DISPLAY_TOAST_ACTION,
  LOADING_SPINNER_SHOW_MUTATION,
  AUTO_NOTIFICATION_USER_ACTION,
  IS_TOKEN_ACTIVE,
  SET_STOMP_DISCONNECT,
  OTP_VERIFY_SHOW_MUTATION,
  BRANCH_VERIFY_SHOW_MUTATION,
  BRANCH_CHECK_ACTION,
} from "../storeconstants";
import apiConfig from "@/api/config/apiConfig";
import { decodeJWT } from "@/plugins/decodeJWT";
import router from "@/routes/router";
import toast from "@/resouce/toast";
let timer = "";
export default {
  async [LOGIN_WITH_GOOGLE_ACTION](context, payload) {
    await context.commit(LOADING_SPINNER_SHOW_MUTATION, true, {
      root: true,
    });
    try {
      await fetch(`${apiConfig}/api/auth/by-google`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `${payload.credential}`,
        },
      })
        .then((response) => response.json().catch(() => response))
        .then(async (response) => {
          await context.dispatch(DECODE_TOKEN, response);
          await context.dispatch(DISPLAY_TOAST_ACTION, 1);
        })
        .catch(async (error) => {
          console.log(error);
          await context.dispatch(DISPLAY_TOAST_ACTION, 2);
        });
    } catch (error) {
      console.log(error);
      await context.dispatch(DISPLAY_TOAST_ACTION, 2);
    }
    await context.commit(LOADING_SPINNER_SHOW_MUTATION, false, {
      root: true,
    });
  },
  async [DISPLAY_TOAST_ACTION](context, payload) {
    if (payload == 1) {
      await context.commit(
        TOAST_SHOW_MUTATION,
        {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: toast.ToastMessAdd_sus,
        },
        {
          root: true,
        }
      );
    } else {
      await context.commit(
        TOAST_SHOW_MUTATION,
        {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: toast.ToastMess_faild,
        },
        {
          root: true,
        }
      );
    }
  },
  async [LOGIN_WITH_FACEBOOK_ACTION](context) {
    await context.commit(LOADING_SPINNER_SHOW_MUTATION, true, {
      root: true,
    });
    try {
      const { authResponse } = await new Promise((resolve, reject) => {
        FB.login(resolve, { scope: "email" });
        console.log(reject);
      });
      if (!authResponse) {
        console.log("close login facebook popup");
      } else {
        FB.api("/me", { fields: "name,email" }, async function (response) {
          if (response && !response.error) {
            await fetch(`${apiConfig}/api/auth/by-facebook`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `${authResponse.accessToken}`,
              },
            })
              .then((response) => response.json().catch(() => response))
              .then(async (response) => {
                await context.dispatch(DECODE_TOKEN, response);
                await context.dispatch(DISPLAY_TOAST_ACTION, 1);
              })
              .catch(async (error) => {
                console.log(error);
                await context.dispatch(DISPLAY_TOAST_ACTION, 2);
              });
          }
        });
      }
    } catch (error) {
      console.log(error);
      await context.dispatch(DISPLAY_TOAST_ACTION, 2);
    }
    await context.commit(LOADING_SPINNER_SHOW_MUTATION, false, {
      root: true,
    });
  },
  async [LOGIN_ACTION](context, payload) {
    await context.dispatch(DECODE_TOKEN, payload);
  },
  [LOGOUT_ACTION](context) {
    context.commit(SET_USER_TOKEN_DATA_MUTATION, {
      access_token: null,
      refresh_token: null,
      sub: null,
      exp: null,
      iat: null,
      role: null,
      email: null,
      username: null,
    });
    localStorage.removeItem("userData");
    context.commit(SET_STOMP_DISCONNECT, {}, { root: true });
    if (timer) {
      clearTimeout(timer);
    }
    router.go("/login");
  },
  [AUTO_LOGIN_ACTION](context) {
    let userDataString = localStorage.getItem("userData");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      context.dispatch(IS_TOKEN_ACTIVE, userData.access_token);
      const currentTimestamp = new Date().getTime();
      const expirationTimestamp = userData.exp * 1000;
      console.log("exp" + expirationTimestamp);
      const waitTime = expirationTimestamp - currentTimestamp;
      //lấy thời gian hết hạn nhỏ hơn 10,000 mili giây thì tự động logout
      if (waitTime < 10000) {
        alert(
          "phiên làm việc của bạn đã kết thúc vui lòng đăng nhập lại để tiếp tục sử dụng ứng dụng"
        );
        context.dispatch(AUTO_LOGOUT_ACTION);
      }
      //nếu không thì tự động thêm vào thời gian hết hạn có sẵn
      else {
        timer = setTimeout(() => {
          context.dispatch(AUTO_LOGOUT_ACTION);
        }, waitTime);
      }
      context.commit(SET_USER_TOKEN_DATA_MUTATION, userData);
    }
  },
  async [IS_TOKEN_ACTIVE](context, payload) {
    try {
      await fetch(`${apiConfig}/users/my-profile`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${payload}`,
        },
      })
        .then((data) => data.json().catch(() => data))
        .then(async (response) => {
          if (response.status && response.status != 200) {
            await context.dispatch(AUTO_LOGOUT_ACTION);
          }
        })
        .catch(async () => {
          await context.dispatch(AUTO_LOGOUT_ACTION);
        });
    } catch (error) {
      await context.dispatch(AUTO_LOGOUT_ACTION);
    }
  },
  [AUTO_LOGOUT_ACTION](context) {
    context.dispatch(LOGOUT_ACTION);
    context.commit(SET_AUTO_LOGOUT_MUTATION);
  },
  async [SET_EXPIRATION_TIME_ACTION](context, payload) {
    const currentTimestamp = new Date().getTime();
    const expirationTimestamp = payload.exp * 1000;
    const waitTime = expirationTimestamp - currentTimestamp;
    if (waitTime > 0) {
      await new Promise((resolve) => {
        timer = setTimeout(async () => {
          console.log("call refresh");
          // Gọi API refresh token
          try {
            const response = await fetch(
              `${apiConfig}/api/auth/refresh-token`,
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${payload.access_token}`,
                },
              }
            );
            const data = await response.json();
            context.dispatch(DECODE_TOKEN, data);
          } catch (error) {
            console.log(error);
          }
          resolve(); // Đánh dấu Promise đã hoàn thành
        }, waitTime);
        if (
          payload.role == "ADMIN" ||
          payload.role == "MANAGER" ||
          payload.role == "STAFF"
        ) {
          router.push("/admin/users");
        } else window.location.replace("/");
      });
    }
  },
  async [DECODE_TOKEN](context, payload) {
    const result = decodeJWT(payload.access_token);
    let userData = {
      access_token: payload.access_token,
      refresh_token: payload.refresh_token,
      sub: result.sub,
      exp: result.exp,
      iat: result.iat,
      role: result.role,
      type: result.type,
      branchId: result.branchId,
      email: result.email,
      username: result.username,
    };
    await context.commit(SET_USER_TOKEN_DATA_MUTATION, userData);
    if (result["isVerify"] === "false") {
      await context.commit(OTP_VERIFY_SHOW_MUTATION, false);
    } else {
      localStorage.setItem("userData", JSON.stringify(userData));
      await context.dispatch(SET_EXPIRATION_TIME_ACTION, userData);
      await context.commit(OTP_VERIFY_SHOW_MUTATION, true);
      await context.dispatch(
        AUTO_NOTIFICATION_USER_ACTION,
        {},
        {
          root: true,
        }
      );
    }
  },
  [BRANCH_CHECK_ACTION](context) {
    let userDataString = localStorage.getItem("userData");
    if (userDataString) {
      let userData = JSON.parse(userDataString);
      if (
        userData["branchId"] === "NOT_YET" &&
        (userData["type"] === "GOOGLE" || userData["type"] === "FACEBOOK") &&
        userData["role"] === "USER"
      ) {
        context.commit(BRANCH_VERIFY_SHOW_MUTATION, userData["branchId"]);
      }
    }
  },
};
