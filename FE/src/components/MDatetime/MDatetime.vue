<template>
  <div class="mdatetime-container">
    <label for="input" class="text">{{ inputLabel }}</label>
    <input
      type="date"
      class="item-input in3"
      :value="formatfromUnixTime(modelValue)"
      @input="debouncedHandleInput"
      :tabindex="tab"
      ref="inputDate"
    />
    <div class="alertInputEm" v-if="isShowAlert">
      {{ AlertMess }}
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import debounce from "lodash/debounce";
export default {
  props: ["modelValue", "tab", "inputLabel"],

  setup(props, { emit }) {
    const AlertMess = ref("This is the default message");
    const isShowAlert = ref(false);
    // Hàm debounce
    const debouncedHandleInput = debounce(function (event) {
      handleInput.call(this, event);
    }, 1000);

    /**
     * hàm lưu giá trị nhập
     */
    function handleInput(event) {
      try {
        const selectedDate = event.target.valueAsDate;
      // Kiểm tra nếu ngày được chọn lớn hơn ngày hiện tại
      if (selectedDate > new Date()) {
        updateAlertMess("Ngày sinh phải trước ngày hôm nay");
        isShowAlert.value = true;
        return;
      } else {
        isShowAlert.value = false;
      }

      emit("update:modelValue", event.target.value);
      emit("getDateTime", this.formattoUnixTime(event.target.valueAsNumber));
      console.log(event.target.valueAsNumber);
      } catch (error) {
        console.log(error);
      }
    }

    function updateAlertMess(message) {
      AlertMess.value = message;
    }

    /**
     * Hàm validate từ date sang unix time
     */
    function formattoUnixTime(date) {
      try {
        const toUnixTime = (year, month, day) => {
          const date = new Date(Date.UTC(year, month - 1, day));
          return Math.floor(date.getTime() / 1000);
        };
        if (date) {
          date = new Date(date);
          // Lấy ra ngày
          let day = date.getDate();
          day = day < 10 ? `0${day}` : day;
          // Lấy ra tháng
          let month = date.getMonth() + 1;
          month = month < 10 ? `0${month}` : month;
          // Lấy ra năm
          let year = date.getFullYear();
          if (year < 1970) {
            year = 1970;
          }
          if (year > 2038) {
            year = 2038;
          }
          const unixTimestamp = toUnixTime(year, month, day);
          return unixTimestamp;
        } else {
          return "";
        }
      } catch (error) {
        console.log(error);
      }
    }

    /**
     * Hàm validate từ unix time sang date
     */
    function formatfromUnixTime(unixTimestamp) {
      try {
        let date = new Date(unixTimestamp * 1000);
        let day = date.getDate();
        if (day < 10) {
          // Thêm một số 0 vào đầu của day
          day = "0" + day;
        }
        let month = date.getMonth() + 1;
        if (month < 10) {
          // Thêm một số 0 vào đầu của day
          month = "0" + month;
        }
        let year = date.getFullYear();
        if (year < 1970) {
          year = 1970;
        }
        if (year > 2038) {
          year = 2038;
        }

        // Format the date as dd/mm/yyyy
        const formattedDate = `${year}-${month}-${day}`;
        return formattedDate;
      } catch (error) {
        console.log(error);
      }
    }
    return {
      debouncedHandleInput,
      handleInput,
      formattoUnixTime,
      formatfromUnixTime,
      AlertMess,
      isShowAlert,
    };
  },
};
</script>

<style>
.mdatetime-container {
  position: relative;
  width: 100%;
  height: 100%;
}
label.text {
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
.item-input.in3 {
  position: static;
  width: 100%;
  height: 100%;
  min-height: 32px;
  width: 100%;
}
.alertInputEm {
  width: 102%;
  height: 16px;
  font-size: 12px;
  position: absolute;
  top: 100%;
  right: -2%;
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