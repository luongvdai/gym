<template>
  <GoogleLogin :callback="handleLoginGoogle" :buttonConfig="gg_config" />
</template>

<script>
import { LOGIN_WITH_GOOGLE_ACTION } from "@/stores/modules/storeconstants";
import { mapActions } from "vuex";
export default {
  data() {
    return {
      gg_config: {
        type: "icon",
        shape: "circle",
      },
      handleLoginGoogle: (response) => {
        this.gg_login(response);
      },
    };
  },
  methods: {
    ...mapActions("auth", {
      gg_login: LOGIN_WITH_GOOGLE_ACTION,
    }),
  },
};
</script>

<style scoped></style>
