import mutations from "./mutations";
import getters from "./getters";
import actions from "./actions";
export default {
  namespaced: true,
  state() {
    return {
      access_token: "",
      refresh_token: "",
      sub: "",
      exp: "",
      iat: "",
      role: "",
      email: "",
      username: "",
      autoLogout: false,
      isVerify: true,
      hasBranch: "",
    };
  },
  mutations,
  getters,
  actions,
};
