<template>
  <div class="toolbar-search">
    <input
      type="text"
      class="toolbar-input-search"
      :placeholder="placeholder"
      :value="InputWhere"
      :disabled="disable"
      :style="style"
      @focus="handleInputFocus"
      ref="inputSearch"
    />
    <button :class="iconsearch"></button>
  </div>
</template>

<script>
import debounce from "lodash/debounce";
export default {
  props:{
    placeholder: String,
    style: String,
    iconsearch: String,
    disable: Boolean,
  },
  mounted() {
    // Thêm một event listener cho input
    this.$refs.inputSearch.addEventListener("input", debounce(this.handleInput, 700));
  },
  methods:{
        /**
         * hàm cập nhật giá trị tìm kiếm
         */
    handleInput(event) {
      this.$emit("InputWhere", event.target.value);
    },
    handleInputFocus() {
      // Khi ô tìm kiếm được focus, gọi sự kiện @InputWhere
      this.$emit("InputWhere", "");
    },

  },data(){
    return{

    }
  }
}

</script>

<style>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.toolbar-search {
  position: relative;
}
.toolbar-input-search {
  height: 33px;
  width: 200px;
  border-width: 1px;
  border-radius: 4px;
  border-color: #bbbbbb;
  color: #000;
  padding: 0 8px;
  border-style: solid;
  outline: unset;
  font-style: italic;
  font-size: 12px;
}
.toolbar-input-search:focus {
  border: 1px solid #50B83C;
}
.icon-search {
  width: 28px;
  height: 28px;
  position: absolute;
  right: 5px;
  border: none;
  background-color: #fff;
  top: 2px;
}
.icon-search {
  background-image: var(--icon);
  background-position: -425px -30px;
  background-repeat: no-repeat;
}.icon-search.b{
    right: 10px;
}
.icon-search.c{
    right: 3px;
}
</style>