<template>
  <div class="cover_toast" v-if="text">
    <div
      class="toast"
      :class="classcssicon == 'toast_icon_failed' ? 'failed' : 'success'"
    >
      <div :class="classcssicon"></div>
      <div class="toast_text">
        <div :class="classcss">{{ text_color }}</div>
        <div class="toast_text_noti">{{ text }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    text: String,
    text_color: String,
    classcss: String,
    classcssicon: String,
  },

  methods: {},
  data() {
    return {};
  },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}

.cover_toast {
  position: fixed;
  top: 0;
  right: 15px;
  z-index: 900;
}
.toast {
  min-width: 300px;
  min-height: 50px;
  z-index: 1200;
  border-radius: 8px;
  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
  background-color: #fff;
  position: relative;
  display: flex;

  animation: slideInLeft ease 0.7s, fadeOut linear 1s 3s forwards;
}
.toast.failed {
  border-left: 4px solid #da2121;
}
.toast.success {
  border-left: 4px solid #50b83c;
}
.toast_icon-success {
  align-self: center;
  width: 24px;
  height: 24px;
  margin: 0 10px;
  background-image: var(--icon);
  background-position: -848px -312px;
  background-repeat: no-repeat;
}
.toast_icon_failed {
  align-self: center;
  width: 24px;
  height: 24px;
  margin: 0 10px;
  background-image: var(--icon);
  background-position: -1552px -312px;
  background-repeat: no-repeat;
}
.toast_text {
  text-align: left;
}
.toast_text_color-success {
  color: #50b83c;
  font-size: 14px;
  font-weight: 900;
}
.toast_text_color-failed {
  color: #da2121;
  font-size: 14px;
  font-weight: 900;
}
.toast_button {
  position: absolute;
  top: 15px;
  right: 15px;
  width: 24px;
  height: 24px;
  background-image: var(--icon);
  background-position: -147px -147px;
  background-repeat: no-repeat;
}
@keyframes slideInLeft {
  from {
    transform: translateY(calc(-60px));
  }
  to {
    opacity: 1;
    transform: translateY(0px);
  }
}

@keyframes fadeOut {
  from {
    transform: translateY(0px);
  }
  to {
    opacity: 0;
    transform: translateY(calc(-60px));
  }
}
</style>
