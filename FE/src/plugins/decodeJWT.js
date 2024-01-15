import { AUTO_LOGOUT_ACTION } from "@/stores/modules/storeconstants";
import store from "@/stores/modules/store";
//giải mã Token
export function decodeJWT(token) {
  try {
    const parts = token.split(".");
    if (parts.length !== 3) {
      throw new Error("Invalid JWT format");
    }

    const base64Url = parts[1];
    if (typeof base64Url === "undefined") {
      throw new Error("Invalid JWT format");
    }

    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const payload = JSON.parse(window.atob(base64));
    return payload;
  } catch (error) {
    console.log(error);
    store.dispatch(`auth/${AUTO_LOGOUT_ACTION}`);
  }
}
