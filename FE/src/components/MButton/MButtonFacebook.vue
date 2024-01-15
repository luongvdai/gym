<template>
  <button class="facebook-btn" @click="fb_login">
    <img
      src="https://cdn-icons-png.flaticon.com/512/5968/5968764.png"
      alt="img_fb_icon"
      height="20"
    />
  </button>
</template>

<script>
import { mapActions } from "vuex";
import { LOGIN_WITH_FACEBOOK_ACTION } from "@/stores/modules/storeconstants";
export default {
  methods: {
    ...mapActions("auth", {
      fb_login: LOGIN_WITH_FACEBOOK_ACTION,
    }),
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
button {
  width: fit-content;
  height: fit-content;
  padding: 9px;
  border-radius: 100%;
  display: flex;
  border: none;
  border: 1px solid #dadce0;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  margin: 0 10px;
}
button:hover {
  cursor: pointer;
  background-color: rgba(240, 240, 255, 0.1);
}
</style>
