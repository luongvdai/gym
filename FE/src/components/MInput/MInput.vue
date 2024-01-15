<template>
  <div class="minput-container">
    <label for="input" class="text">{{ inputLabel }}</label>
    <input
      v-if="typeInput !== 'password'"
      :type="typeInput"
      :style="style"
      class="item-input"
      :class="{
        'item-input-red': inValueChange == false,
        'item-input-green': inValueChange == true,
      }"
      :placeholder="placeholder"
      :value="modelValue"
      :tabindex="tab"
      :maxlength="maxlength"
      :readOnly="readOnly"
      :autocomplete="autocomplete"
      ref="input"
      :min="typeInput === 'number' ? 0 : null"
    />
    <input
      v-else
      :type="passwordVisible ? 'text' : 'password'"
      :style="style"
      class="item-input"
      :class="{
        'item-input-red': inValueChange == false,
        'item-input-green': inValueChange == true,
      }"
      :placeholder="placeholder"
      :value="modelValue"
      :tabindex="tab"
      :maxlength="maxlength"
      :readOnly="readOnly"
      :autocomplete="autocomplete"
      ref="input"
      :min="typeInput === 'number' ? 0 : null"
    />
    <div
      class="toggle-password"
      v-if="typeInput === 'password'"
      @click="togglePasswordVisibility"
    >
      <span
        v-html="
          passwordVisible
            ? '<i class=\'ti ti-eye-off\'></i>'
            : '<i class=\'ti ti-eye\'></i>'
        "
      ></span>
    </div>
    <div class="alertInputEm" v-if="isShowAlert">
      {{ AlertMess }}
    </div>
  </div>
</template>
<script>
import regex from "@/resouce/regex";
import debounce from "lodash/debounce";
export default {
  props: {
    //giá trị truyền vào
    modelValue: String,
    toolTip: String,
    tab: Number,
    refs: String,
    maxlength: Number,
    //loại input, regex
    typeInput: String,
    NumberDecimal: Boolean,
    //style cho input
    style: String,
    //số lượng từ tối đa
    maxValue: Number,
    placeholder: String,
    //tên input
    inputLabel: String,
    //có cho nhập hay không
    readOnly: Boolean,
    //trạng thái border input
    inValue: {
      type: Boolean,
      default: true,
    },
    autocomplete: String,
  },

  mounted() {
    // Thêm một event listener cho input
    this.$refs.input.addEventListener("input", debounce(this.handleInput, 800));
    //kiểm tra trong $refs và delay 1,2s
    this.$emit("isSubmit", this.modelValue ? true : false, this.tab);
  },

  methods: {
    /**
     * hàm lưu giá trị nhập
     */
    handleInput(event) {
      this.inValueChange = this.inValue;
      this.ValueInput = event.target.value;
      //kiểm tra có validate thành công không
      if (this.validateInput(this.ValueInput)) {
        this.$emit("update:modelValue", this.ValueInput);
        this.$emit("updateAlert");
        //có thì thay đổi trạng thái và update lên input
        this.$emit("isSubmit", true, this.tab);
      } else {
        this.$emit("update:modelValue", this.ValueInput);
        this.$emit("updateAlert");
        this.$emit("isSubmit", false, this.tab);
      }
    },

    togglePasswordVisibility() {
      this.passwordVisible = !this.passwordVisible;
    },
    /**
     * Hàm validate input
     */
    validateInput(value) {
      var validate = true;
      //trường hợp trống
      if (value == "") {
        this.inValueChange = false;
        this.isShowAlert = true;
        this.AlertMess = "Không được để trống";
        return (validate = false);
      } else {
        //trường hợp không trống
        if (!this.regexInput(value)) {
          this.inValueChange = false;
          this.isShowAlert = true;
          return (validate = false);
          //trường hợp không cần regex
        } else {
          this.inValueChange = true;
          this.isShowAlert = false;
        }
      }

      return validate;
    },

    /***
     * Hàm validate bằng regex
     */
    regexInput(value) {
      var regex = true;
      //loại username
      if (this.typeInput == "username") {
        if (!this.validUserName(value)) {
          this.AlertMess = "Không có ký tự đặc biệt, khoảng trắng";
          regex = false;
        }
      }
      //loại email
      if (this.typeInput == "email") {
        if (!this.validEmail(value)) {
          this.AlertMess = "Không đúng định dạng email";
          regex = false;
        }
      }
      //loại password
      if (this.typeInput == "password") {
        if (!this.validPassword(value)) {
          this.AlertMess = "Tối thiểu 6 ký tự, 1 chữ, 1 số";
          regex = false;
        }
      }
      //loại sdt
      if (this.typeInput == "phonenumber") {
        if (!this.validPhoneNumber(value)) {
          this.AlertMess = "Không đúng định dạng sdt";
          regex = false;
        }
      }
      //loại code forgot pass
      if (this.typeInput == "code") {
        if (!this.validCode(value)) {
          this.AlertMess = "Không đúng định dạng mã";
          regex = false;
        }
      }

      return regex;
    },
    /**
     * hàm regex kiểm tra username
     */
    validUserName(username) {
      var re = regex.RegexUserName;
      return re.test(username);
    },

    validPhoneNumber(phonenumber) {
      var re = regex.RegexPhone;
      return re.test(phonenumber);
    },

    /**
     * hàm regex kiểm tra username
     */
    validPassword(password) {
      var re = regex.RegexPassword;
      return re.test(password);
    },
    /**
     * hàm regex kiểm tra username
     */
    validEmail(email) {
      var re = regex.RegexEmail;
      return re.test(email);
    },
    /**
     * hàm regex kiểm tra username
     */
    validCode(code) {
      var re = regex.RegexCode;
      return re.test(code);
    },
  },
  data() {
    return {
      //trạng thái đỏ input
      inValueChange: {
        type: Boolean,
        default: null,
      },

      AlertMess: "",
      isShowAlert: false,
      //lưu giá trị input
      ValueInput: null,
      //lưu thời gian delay khi nhập
      timeout: null,
      passwordVisible: false,
    };
  },
};
</script>
<style scoped>
.minput-container {
  height: 100%;
  width: 100%;
  position: relative;
}
.text {
  font-size: 0.75rem;
  color: #2ca01c;
  font-weight: 700;
  position: absolute;
  margin: -3px 0 0 7px;
  padding: 0 3px;
  width: fit-content;
  background: #ffffff;
}
.item-input {
  position: static;
  min-height: 32px;
  width: 100%;
  border: 1px solid #bbbbbb;
  margin-top: 5px;
  outline: none;
  padding-left: 10px;
  border-radius: 3px;
}
.item-input:focus {
  border: 1px solid #50b83c;
  outline: none;
}
.item-input-red {
  border: 1px solid #ff0000;
}
.item-input-green {
  border: 2px solid #50b83c;
}
.toggle-password {
  position: absolute;
  top: 23px;
  right: 10px;
  transform: translateY(-50%);
  cursor: pointer;
  color: #555;
  font-size: 20px;
}

.alertInputEm {
  width: 70%;
  height: 16px;
  font-size: 12px;
  position: absolute;
  top: 76%;
  right: 15%;
  background-color: #ff7777;
  border-radius: 4px;
  padding: 0 4px;
  z-index: 10;
  text-align: center;
  color: #fff;
}
.alertInputEm::after {
  content: " ";
  position: absolute;
  top: -15px;
  right: 45%;
  border-width: 9px 9px;
  border-style: solid;
  border-radius: 4px;
  border-color: transparent transparent #ff7777 transparent;
  cursor: pointer;
}
</style>
