import {
  SET_AUTO_LOGOUT_MUTATION,
  SET_USER_TOKEN_DATA_MUTATION,
  OTP_VERIFY_SHOW_MUTATION,
  BRANCH_VERIFY_SHOW_MUTATION,
} from "../storeconstants";

export default {
  [SET_USER_TOKEN_DATA_MUTATION](state, payload) {
    state.access_token = payload.access_token;
    state.refresh_token = payload.refresh_token;
    state.sub = payload.sub;
    state.exp = payload.exp;
    state.iat = payload.iat;
    state.role = payload.role;
    state.email = payload.email;
    state.username = payload.username;
    state.autoLogout = false;
  },
  [SET_AUTO_LOGOUT_MUTATION](state) {
    state.autoLogout = true;
  },
  [OTP_VERIFY_SHOW_MUTATION](state, payload) {
    state.isVerify = payload;
  },
  [BRANCH_VERIFY_SHOW_MUTATION](state, payload) {
    state.hasBranch = payload;
  },
};
