<template>
  <div id="page-admin" class="admin-container">
    <the-header class="header"></the-header>
    <the-sidebar class="sidebar"></the-sidebar>
    <the-content class="content"></the-content>
  </div>
</template>

<script>
import TheHeader from "@/components/LayoutAdmin/TheHeader/TheHeader.vue";
import TheSidebar from "@/components/LayoutAdmin/TheSidebar/TheSidebar.vue";
import TheContent from "@/components/LayoutAdmin/TheContent/TheContent.vue";

export default {
  name: "AdminPage",
  components: {
    TheHeader,
    TheSidebar,
    TheContent,
  },
};
</script>

<style scoped>
.admin-container {
  z-index: 1;
  position: absolute;
  font-size: 14px;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  background-color: #eceef1;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
}
* {
  margin: 0;
  padding: 0;
}

.header {
  order: 2;
  height: 56px;
  float: left;
  clear: right;
  background-color: #ffff;
}
.content {
  order: 3;
  width: calc(100% - 130px);
  height: calc(100vh - 88px);
  float: right;
  background-color: rgb(236, 238, 241);
  padding: 0 24px;
  padding-top: 24px;
  position: relative;
}
.popup {
  position: fixed;
  width: 100%;
  height: 100vh;
  right: 0;
  left: 0;
  top: 0;
  bottom: 0;
  background-color: rgba(12, 12, 12, 0.6);
}

@media (max-width: 1200px) {
  .content {
    width: calc(100% - 100px);
  }
}
</style>
