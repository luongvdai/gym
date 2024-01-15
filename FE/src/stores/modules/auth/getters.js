import {
  GET_USER_TOKEN_GETTER,
  IS_AUTHENTICATE_GETTER,
  OTP_IS_VERIFY_GETTER,
  BRANCH_IS_VERIFY_GETTER,
} from "../storeconstants";

export default {
  [GET_USER_TOKEN_GETTER]: (state) => {
    return state.access_token;
  },
  [IS_AUTHENTICATE_GETTER](state) {
    return !!state.access_token;
  },
  [OTP_IS_VERIFY_GETTER](state) {
    return state.isVerify;
  },
  [BRANCH_IS_VERIFY_GETTER](state) {
    return state.hasBranch;
  },
};
