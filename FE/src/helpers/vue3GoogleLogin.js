// vue3GoogleLogin.js
import vue3GoogleLogin from "vue3-google-login";

let CLIENT_ID =
  "383558103335-kbr7cvg530d1oc8iq3h2ftcu8i6fqk2f.apps.googleusercontent.com";

export default function useVue3GoogleLogin(app) {
  app.use(vue3GoogleLogin, { clientId: CLIENT_ID });
}
