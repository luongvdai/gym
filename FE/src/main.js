import { createApp } from "vue";
import App from "./App.vue";
import router from "@/routes/router";
import store from "./stores/modules/store";
import useVue3GoogleLogin from "./helpers/vue3GoogleLogin";
import { initFacebookSdk } from "./helpers/init-facebook-sdk";
import vuetify from "./helpers/vuetify";
import CKEditor from "@ckeditor/ckeditor5-vue";
const app = createApp(App);
app.use(store);

initFacebookSdk().then(startApp);
app.use(router);
app.use(vuetify);
app.use(CKEditor);
useVue3GoogleLogin(app);
function startApp() {
  app.mount("#app");
}
