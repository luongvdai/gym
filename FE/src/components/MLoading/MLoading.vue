<template>
  <div class="cover-loading">
    <div class="loading-icon icon"></div>
  </div>
</template>

<style scoped>
:root {
  --loading-icon: url('@/assets/icons/loading.svg');
}
.cover-loading {
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
.loading-icon {
  width: 50px;
  height: 50px;
  position: fixed;
  top: 50%;
  right: 50%;
}
.icon {
  background-image: var(--loading-icon);
  background-repeat: no-repeat;
  background-size: 50px;

  animation: spin 1s linear 0s infinite;
  -webkit-animation: spin 1s linear 0s infinite;
  -moz-animation: spin 1s linear 0s infinite;
  -o-animation: spin 1s linear 0s infinite;
}

@-webkit-keyframes spin {
  from {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -o-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -o-transform: rotate(360deg);
  }
}
/* Standard syntax */
@keyframes spin {
  from {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -o-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -o-transform: rotate(360deg);
  }
}
</style>