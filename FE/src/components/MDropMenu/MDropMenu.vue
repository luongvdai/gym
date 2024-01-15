<template>
  <div class="sub-menu">
    <div v-for="(item, i) in items" :key="i" class="sub-item">
      <router-link :to="item.linkRouter" v-if="item.linkRouter">{{ item.name }} </router-link>
      <router-link to="/branchdetails" v-if="!item.linkRouter">{{ item.name }} </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "MDropMenu",
  setup() {},
  props: ["items"],
};
</script>
<style scoped>
.sub-menu {
  z-index: 1;
  position: absolute;
  background-color: #fff;
  top: calc(100% + 4px);
  /* left: calc(100% - 85px); */
    left: 0% ;
  width: max-content;
  border: 1.5px solid #9e9e9e;
}

.sub-item {
  text-align: left;
  padding: 8px 20px 8px 10px;
}
.sub-item:last-child {
  border-bottom: 0px;
}

.sub-item a {
  text-decoration: none;
  font-weight: 200;
  font-size: 15px;
  color: black;
}
.sub-item:hover {
  background-color: #efeaea;
}
</style>
