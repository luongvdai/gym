<template>
  <div class="cover-popup">
    <div class="popup-top-right btn">
      <button class="btn-exit" @click="closePopup">
        <i class="ti ti-x"></i>
      </button>
    </div>
    <div class="popup-center">
      <v-carousel hide-delimiters :show-arrows="false">
        <v-carousel-item :src="url" cover></v-carousel-item>
      </v-carousel>
    </div>
  </div>
</template>

<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  name: "MPopupViewImg",
  props: {
    urlImg: String,
  },
  created() {
    if (this.urlImg) {
      (async () => {
        this.url = await fireStorage.displayImage(this.urlImg);
      })();
    }
  },
  methods: {
    closePopup() {
      this.$emit("close-popupimg-click");
    },
  },
  data() {
    return {
      url: "",
    };
  },
};
</script>

<style scoped>
.cover-popup {
  position: fixed;
  width: 100%;
  height: 100vh;
  right: 0;
  left: 0;
  top: 0;
  bottom: 0;
  background-color: rgba(12, 12, 12, 0.3);
  z-index: 1000;
}
.popup-center {
  height: 100%;
  position: absolute;
  left: 27%;
  right: 0px;
  margin: 0 auto;
  border-radius: 4px;
  padding: 5% 0%;
}
.v-window {
  width: 75% !important;
  height: 100% !important;
}
.v-img__img {
  width: auto !important;
  height: auto !important;
}
.v-img__img--cover {
  object-fit: contain !important;
}
.popupimg {
  height: 100%;
  position: absolute;
  left: 15%;
  right: 0px;
  top: 8%;
  margin: 0 auto;
  border-radius: 4px;
}
.popup-top-right.btn {
  width: 3%;
  height: 33%;
  position: absolute;
  right: 0;
  z-index: 2;
}
.btn-exit {
  width: 30px;
  height: 30px;
  border: none;
  background: #fff;
  font-size: 25px;
  position: absolute;
  right: 140%;
  top: 22%;
}
</style>
