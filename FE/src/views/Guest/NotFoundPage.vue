<template>
  <div class="notFound">
    <h1>404</h1>
    <h4>Không tìm thấy trang</h4>
  </div>
</template>

<script>
export default {
  name: "notFound",
  created() {},
};
</script>

<style scoped>
.notFound {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
h1{
  font-size: 5em;
  font-weight: bolder;
}
</style>
