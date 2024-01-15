<template>
    <label class="con-radio">
    <input
      :tabindex="tab"
      :name="name"
      :value="value"
      :checked="checked"
      type="radio"
      class="radio--input"
      @click="handleClick"
    />
    <span class="radio">
      <span class="radio-focus"></span>
      <span
        :class="{ 'radio-border-checked-true': checked }" 
        class="radio-border"
      ></span>
      <span
        :class="{ 'radio-circle-checked-true': checked }"
        class="radio-circle"
      ></span>
    </span>
    <span class="radio--label">{{ label }}</span>
  </label>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
    },
    checked: {
      type: Boolean,
      default: false,
    },
    value: {
      type: Number,
    },
    name: {
      type: String,
    },
    tab: {
      type: Number,
      default: -1,
    },
  },
  emits: ["click-radio"],
  setup(props, context) {
        /**
         * hàm cập nhật giá trị giới tính
         * Nguyễn Văn Cương 09/09/2022
         */
    const handleClick = () => {
      context.emit("click-radio", props.value); 
    };
    return {
      handleClick,
    };
  },
};
</script>

<style scoped>
.con-radio {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  -webkit-box-align: center;
  -webkit-box-pack: center;
}
.radio--input {
  position: absolute;
  opacity: 0;
  width: 20px;
}
.radio--input:focus + .radio .radio-focus {
  position: absolute;
  width: 18px;
  height: 18px;
  top: 0;
  left: 0;
  border-radius: 50%;
  border: 1px solid #2ca01c;
}
.radio {
  position: relative;
  width: 18px;
  height: 18px;
  display: block;
  border-radius: 50%;
  cursor: pointer;
  margin-left: 10px;
}
.radio-border-checked-true {
  border: 1px solid #2ca01c;
}
.radio-border {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 1px solid #afafaf;
  background: #fff;
  z-index: 1;
  justify-content: center;
  display: block;
}
.radio-circle-checked-true {
  display: block;
  position: absolute;
  width: 10px;
  height: 10px;
  top: 3px;
  left: 3px;
  border-radius: 50%;
  background: #2ca01c;
}
.radio--input:checked + .radio .radio-circle {
  display: block;
  position: absolute;
  width: 12px;
  height: 12px;
  top: 2.8px;
  left: 3.2px;
  border-radius: 50%;
  background: #2ca01c;
}
.radio-circle-checked-true {
  display: block;
  position: absolute;
  width: 10px;
  height: 10px;
  top: 4px;
  left: 4px;
  border-radius: 50%;
  background: #2ca01c;
}
.radio--label {
  margin-left: 10px;
  cursor: pointer;
  font-size: 13px;
}
</style>