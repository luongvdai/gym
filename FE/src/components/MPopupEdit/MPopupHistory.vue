<template>
  <div class="cover-popup">
    <div class="popupedit" :style="height">
      <div class="popupedit-top">
        <div class="popupedit-top-left">
          <div class="popupedit-top-left-label" :style="width">
            {{ PopupHistory_label }}
          </div>
        </div>
        <div class="popupedit-top-right">
          <div class="popup-top-right btn">
            <button class="btn-exit" @click="closePopup">
              <i class="ti ti-x"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="popupedit-content">
        <div class="history-content-top">
          <label class="history-label name">Thời gian</label>
          <label class="history-label problem">Vấn đề</label>
          <label class="history-label problem">Chi phí bảo trì</label>
        </div>
        <div class="history-content">
          <div class="history-data" v-for="index in History" :key="index">
            <div class="history-label name">
              {{ formatUnixTime(index.createdDate) }}
            </div>
            <div class="history-label problem">{{ index.problem }}</div>
            <div class="history-label problem">
              {{ FormatVND(index.maintenanceCost) }}
            </div>
          </div>
          <div class="history-input">
            <div class="history-label name">
              <MInput
                class="popup-input"
                :tab="1"
                :inValue="inValue_Name"
                :maxlength="10"
                @updateAlert="UpdateAlert"
                v-model="CreatedDate"
                :readOnly="true"
                :style="'border: none;text-align: center;'"
              />
            </div>
            <div class="history-label problem">
              <MInput
                class="popup-input"
                :tab="1"
                :inValue="inValue_Name"
                :maxlength="50"
                :typeInput="'problem'"
                @updateAlert="UpdateAlert"
                :placeholder="'Điền vấn đề'"
                v-model="History.problem"
                :style="'text-align: center;'"
                @isSubmit="checkProblem"
              />
            </div>
            <div class="history-label problem mx-2">
              <MInput
                class="popup-input"
                :tab="2"
                :inValue="inValue_Name"
                maxlength="9"
                :typeInput="'number'"
                :placeholder="'Điền chi phí'"
                @updateAlert="UpdateAlert"
                v-model="History.maintenanceCosts"
                :style="'text-align: center;'"
                @isSubmit="checkProblem"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="popupedit-bottom">
        <div class="popupedit-content-bottom-left">
          <div class="btn-product-popup-save1" @click="addHistory">
            <MButton
              :tab="5"
              :ButtonCss="'content-top-btn-content'"
              :text="'Thêm'"
            />
          </div>
        </div>
        <div class="popupedit-content-bottom-right">
          <div @click="closePopup">
            <MButton :tab="7" :ButtonCss="'btn-button-cancel'" :text="'Hủy'" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MButton from "@/components/MButton/MButton.vue";
import MInput from "@/components/MInput/MInput.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  components: {
    MButton,
    MInput,
  },
  props: {
    height: String,
    PopupHistory_label: String,
    id: String,
  },
  created() {
    this.loadData();
  },
  methods: {
    FormatVND(amount) {
      try {
        // Định dạng số thành chuỗi
        let formattedValue = amount.toString();
        // Chia thành các phần mỗi 3 số và thêm dấu chấm phân cách
        formattedValue = formattedValue.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
        // Thêm đơn vị tiền tệ
        formattedValue += " VNĐ";
        return formattedValue;
      } catch (error) {
        console.log("Lỗi format VNĐ");
      }
    },
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closePopup() {
      this.$emit("close-popup-click");
    },

    formatUnixTime(unixTimestamp) {
      try {
        var date = new Date(unixTimestamp); // Multiply by 1000 to convert to milliseconds
        var day = date.getDate();
        var month = date.getMonth() + 1; // Months are zero-based
        var year = date.getFullYear();

        // Format the date as dd/mm/yyyy
        const formattedDate = `${day}/${month}/${year}`;

        return formattedDate;
      } catch (error) {
        console.log(error);
      }
    },

    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Thêm thành công",
        };

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
        };
        //trường hợp toast hành động thất bại
      } else {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: message,
        };
      }
      this.displayToast(this.ToastInfor);
    },

    async loadData() {
      try {
        fetch(
        `${apiConfig}/device-update-histories?deviceId=` + this.id,
        await fetchOptions("GET")
      )
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          this.History = data.content;
        })
        .catch((res) => {
         console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async addHistory() {
      if (this.isSubmitProblem) {
        try {
          await fetch(
            `${apiConfig}/device-update-histories`,
            await fetchOptions("POST", {
              problem: this.History.problem,
              maintenanceCost: this.History.maintenanceCosts,
              deviceId: this.id,
            })
          )
            .then((res) => res.json())
            .then((res) => {
              if (!res.id) {
                this.ShowToast(3, res.message);
              } else {
                this.loadData();
                this.ShowToast((this.ToastStatus = 1));
              }
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Thêm thất bại");
            });
        } catch (error) {
          console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
        }
      }
    },
    checkProblem(submit) {
      this.isSubmitProblem = submit;
    },
  },
  data() {
    return {
      History: {},
      CreatedDate: "Hôm nay",
      isSubmitProblem: false,
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
.popupedit {
  width: 500px;
  height: 345px;
  position: relative;
  top: 30%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
}
::-webkit-scrollbar {
  height: 8px;
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #fff;
  border-radius: 4px;
}
/* Handle */
::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 4px;
  cursor: pointer;
  width: 8px;
  height: 8px;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #7c869c;
}
.popupedit-top {
  width: 100%;
  height: 75px;
  display: flex;
  position: relative;
}
.popupedit-top-left {
  width: 50%;
  height: 100%;
}
.popupedit-top-left-label {
  height: 60px;
  width: 100%;
  font-size: 24px;
  font-weight: 700;
  margin-top: 26px;
  margin-left: 30px;
}
.popupedit-top-right {
  position: absolute;
  right: 0;
  display: flex;
  top: 10px;
  width: 70px;
  height: 50px;
}

.popupedit-top-right {
  position: absolute;
  right: 0;
  display: flex;
  top: 10px;
  width: 70px;
  height: 50px;
}
.popupedit-content {
  width: 87%;
  height: 60%;
  border: 1px solid #bbbb;
  margin-left: 32px;
}
.popup-top-right.btn {
  width: 30%;
  height: 100%;
  position: absolute;
  right: 0;
}
.btn-exit {
  width: 30px;
  height: 30px;
  border: none;
  background: #fff;
  font-size: 25px;
  position: absolute;
  right: 140%;
  top: 28%;
}
.popupedit-bottom {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 96%;
  height: 60px;
}
.popupedit-content-bottom-left {
  position: absolute;
  left: 15px;
  width: 30%;
  height: 100%;
  display: flex;
  align-items: center;
}
.popupedit-content-bottom-right {
  position: absolute;
  right: 0;
  width: 26%;
  height: 100%;
  display: flex;
  align-items: center;
}
.history-content-top {
  height: 20%;
  width: 100%;
  display: flex;
  align-items: center;
  font-size: 13px;
  border-bottom: 1px solid #bbbb;
}
.history-label.name {
  width: 40%;
}
.history-label.problem {
  width: 60%;
}
.history-content {
  width: 100%;
  height: 80%;
  overflow: scroll;
}
.history-data {
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 100;
  cursor: pointer;
}
.history-input {
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 100;
  cursor: pointer;
}
.popup-input {
  height: 100%;
}
</style>
