import { GET_USER_TOKEN_GETTER } from "@/stores/modules/storeconstants";
import store from "@/stores/modules/store";
async function fetchOptions(method, body = null) {
  let options = {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
  };
  if (body) {
    try {
      options.body = JSON.stringify(body);
    } catch (error) {
      console.log(error);
    }
  }
  let token = store.getters[`auth/${GET_USER_TOKEN_GETTER}`];
  if (token) {
    options.headers["Authorization"] = `Bearer ${token}`;
  }
  return options;
}

export default fetchOptions;
