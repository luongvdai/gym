<template>
  <div class="contentner" id="opendrop">
    <v-skeleton-loader
      v-if="LoadingShow"
      type="table-tbody"
    ></v-skeleton-loader>
    <table v-else>
      <thead>
        <tr>
          <th v-if="TableCheckBox" class="box">
            <MCheckbox
              @click="handleCheckBoxAll"
              :stateCheckAll="stateCheckAll"
            />
          </th>
          <th
            v-for="index in thListTable"
            :key="index.label"
            :class="index.class"
            :style="index.style"
          >
            {{ index.label }}
          </th>
          <th v-if="TableFun" class="tab-th-select">CHỨC NĂNG</th>
        </tr>
      </thead>

      <tbody v-if="RecordsLoad">
        <tr
          v-for="re in RecordsLoad"
          :key="re[tdListTable[tdListTable.length - 1].property]"
          @dblclick="rowDBClick(re)"
        >
          <td v-if="TableCheckBox" class="box">
            <MCheckbox
              @click="
                handleCheckBox(re[tdListTable[tdListTable.length - 1].property])
              "
              :stateCheckAll="stateCheckAll"
            />
          </td>
          <td
            v-for="index in tdListTable"
            :class="index.class"
            :style="index.style"
            :key="index"
          >
            {{
              index.fun === 1
                ? this.formatStatusRecord(re[index.property])
                : index.fun === 2
                ? this.formatGenderRecord(re[index.property])
                : index.fun === 3
                ? this.formatDateRecord(re[index.property])
                : index.fun === 4
                ? this.formatMonth(re[index.property])
                : index.fun === 5
                ? this.formatObject(re[index.property])
                : index.fun === 6
                ? this.FormatVND(re[index.property])
                : index.fun === 7
                ? this.formatActive(re[index.property])
                : index.fun === 8
                ? this.formatVerify(re[index.property])
                : index.fun === 70
                ? this.formatImg(re[index.property])
                : index.fun === 9
                ? this.formatComplete(re[index.property])
                : index.fun === 10
                ? this.formatConsultingtStatus(re[index.property])
                : index.fun === 11
                ? this.formatUserCourseStatus(re[index.property])
                : index.fun === 12
                ? this.formatUnixTime(re[index.property])
                : index.fun === 999
                ? this.formatUnixTimeHours(re[index.property])
                : re[index.property]
            }}
            <div
              v-if="index.fun == 70"
              @click="openImg(re[index.property])"
              class="td_img"
            ></div>
          </td>
          <td v-if="TableFun" style="min-width: 150px">
            <div
              class="tab-th-select-lable"
              @click="getRecordDetele(re, re[tdListTable[0].property])"
            >
              <MDropItem
                @dropitem-value="openPopupAsk"
                :MDropSta="1"
                :RecordDrop="TableFun"
                :labeldrop="'Sửa'"
                :Drstyle="'left: -50px;'"
                :label="'name'"
              />
            </div>
            <div class="btnopendrop"></div>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskDelete"
        :record="PopupNotilable"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="deleteRecord"
        @close-notification-click="ClosePopupAsk"
        :getRecordCode="getrecorddetetename"
        :MPopupN="PopupNotificationMode"
      />
    </div>
  </div>
</template>

<script>
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MCheckbox from "@/components/MCheckbox/MCheckbox.vue";
import MDropItem from "@/components/MDropItems/MDropItems.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "RecordList",
  props: {
    //object chứa record
    RecordsLoad: Object,
    //trạng thái đóng mở
    closeSelectedAll: Boolean,
    //table theader
    thListTable: [],
    //table tbody
    tdListTable: [],
    //tiêu đề của popup
    PopupNotilable: String,
    //url
    baseURL: String,
    //trạng thái checkbox
    TableCheckBox: Boolean,
    //trạng thái xóa checkbox
    DeleteCheckbox: Boolean,
    //mảng chứa các chức năng
    TableFun: Array,
    //lưu property của object con
    objectTdList: String,
    objectTdListBranch: String,
  },
  components: {
    MDropItem,
    MCheckbox,
    MPopupNotification,
  },
  updated() {},
  mounted() {
    //gọi hàm click outsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  unmounted() {
    //xóa hàm click outsite
    window.removeEventListener("mouseup", this.clickEventInterrupt);
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    openImg(value) {
      this.$emit("Show-Img", value);
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
          message: "Xóa thành công",
        };

        //trường hợp toast cập nhật thành công
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

    /**
     * hàm click outsite
     * Nguyễn Văn Cương 01/10/2022
     */
    clickEventInterrupt(event) {
      //lưu vị trí con chuột left, top
      this.PosY = event.y;
      this.PosX = event.x;
    },

    /**
     * Hàm thực hiện kích hoạt toàn bộ checkbox
     * Nguyễn Văn Cương 15/10/2022
     */
    handleCheckBoxAll() {
      //chuyển trạng thái checkbox
      try {
        this.stateCheckAll = !this.stateCheckAll;
      if (this.stateCheckAll == true) {
        //vòng lặp thêm mã nhân viên vào mảng
        this.RecordsLoad.forEach((re) => {
          this.listReSelected.push(
            re[this.tdListTable[this.tdListTable.length - 1].property]
          );
        }, this.$emit("get-List-Checkbox", this.listReSelected, this.stateCheckAll));
      } else {
        this.listReSelected = [];
        this.$emit(
          "get-List-Checkbox",
          this.listReSelected,
          this.stateCheckAll
        );
      }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * hàm thực hiện kích hoạt checkbox
     */
    handleCheckBox(ReID) {
      //thêm mã nhân viên đã chọn vào mảng
      if (!this.listReSelected.includes(ReID)) {
        this.listReSelected.push(ReID);
      }
      if (this.listReSelected.length == 10) {
        this.handleCheckBoxAll();
      } else {
        this.stateCheckAll = false;
        this.$emit(
          "get-List-Checkbox",
          this.listReSelected,
          this.stateCheckAll
        );
      }
    },
    /**
     * hàm hiện thông tin trên popup khi nhấn vào Sửa
     */
    rowDBClick(recordID) {
      this.$emit("custom-open-dbclick", recordID, 2);
    },

    /**
     * hàm lấy thông tin nhân viên khi xóa
     */
    getRecordDetele(record, recordName) {
      this.getrecorddetetevalue = record;
      this.getrecorddetetename = "<" + recordName + ">";
      this.$emit("handle-click-funtable", record["id"]);
    },

    /**
     * hàm mở popup hỏi người dùng có xóa không
     */
    openPopupAsk(selectedit) {
      if (selectedit == 1) {
        this.isShowAskDelete = true; //hiện popup hỏi người dùng
        this.PopupNotificationMode = 2;
     
      } else {
        //ngưng sử dụng
        this.$emit("change-click", this.getrecorddetetevalue, selectedit);
      }
    },

    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
      this.popupAskCance = true; //lưu trạng thái đóng popup hỏi người dùng
    },

    /**
     * Hàm xóa theo id
     */
    async deleteRecord() {
      try {
        //lấy id đã lưu
        var id = this.getrecorddetetevalue["id"];
        //đóng popup hỏi người dùng
        this.ClosePopupAsk();
        fetch(`${apiConfig}/` + this.baseURL + id, await fetchOptions("DELETE"))
          .then((data) => {
            if (data.status != "200") {
              this.ShowToast(3, data.message);
            } else {
              this.$emit("data-load-delete");
              this.ShowToast((this.ToastStatus = 1));
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Xóa thất bại");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    formatObject(object) {
      try {
        if (object != null) {
          var name = "";
          this.objectTdList.forEach((i) => {
            if (object[i.value]) {
              name = object[i.value];
            }
          });
          return name;
        }
      } catch (error) {
        console.log(error);
      }
    },
    formatImg(img) {
      if (img != null && img != "") {
        return "Xem ảnh";
      }
    },

    /**
     * hàm format ngày tháng
     */
    formatDateRecord(date) {
      try {
        return date;
      } catch (error) {
        console.log(error);
      }
    },

    // formatUnixTime(unixTimestamp) {
    //   try {
    //     //const utcTime = new Date(unixTimestamp); // Nhân với 1000 để chuyển sang milliseconds
    //     //const localTime = new Date(utcTime.getTime() + 7 * 60 * 60 * 1000); // Áp dụng múi giờ Việt Nam (UTC+7)
    //     let localTime = new Date(unixTimestamp);

    //     const day = localTime.getDate();
    //     const month = localTime.getMonth() + 1; // Tháng được đếm từ 0
    //     const year = localTime.getFullYear();
    //     const hours = localTime.getHours();
    //     const minutes = localTime.getMinutes();

    //     const formattedDateTime = `${hours}:${minutes} - ${day}/${month}/${year}`;

    //     return formattedDateTime; // Trả về ngày và giờ đã định dạng
    //   } catch (error) {
    //     console.log(error); // Xử lý lỗi nếu có
    //   }
    // },

    formatUnixTime(unixTimestamp) {
      try {
        // Nhân với 1000 để chuyển đổi từ giây sang mili giây
        const localTime = new Date(unixTimestamp * 1000);

        const day = localTime.getDate();
        // Tháng được đếm từ 0, nên phải cộng thêm 1
        const month = localTime.getMonth() + 1;
        const year = localTime.getFullYear();

        // Kiểm tra nếu ngày hoặc tháng chỉ có một chữ số, thêm '0' phía trước
        const formattedDay = day < 10 ? `0${day}` : `${day}`;
        const formattedMonth = month < 10 ? `0${month}` : `${month}`;

        const formattedDateTime = `${formattedDay}/${formattedMonth}/${year}`;

        return formattedDateTime; // Trả về ngày đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
        return null; // Hoặc có thể trả về một giá trị mặc định khác
      }
    },

    formatUnixTimeHours(unixTimestamp) {
      try {
        // Không cần nhân với 1000 nữa vì unixTimestamp đã ở đơn vị milliseconds
        const localTime = new Date(unixTimestamp);

        const day = localTime.getDate();
        const month = localTime.getMonth() + 1; // Tháng được đếm từ 0
        const year = localTime.getFullYear();
        const hours = localTime.getHours();
        const minutes = localTime.getMinutes();

        // Đảm bảo có hai chữ số cho ngày, tháng, giờ và phút
        const formattedDay = day < 10 ? `0${day}` : day;
        const formattedMonth = month < 10 ? `0${month}` : month;
        const formattedHours = hours < 10 ? `0${hours}` : hours;
        const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;

        const formattedDateTime = `${formattedHours}:${formattedMinutes} - ${formattedDay}/${formattedMonth}/${year}`;

        return formattedDateTime; // Trả về ngày và giờ đã định dạng
      } catch (error) {
        console.log(error); // Xử lý lỗi nếu có
      }
    },
    /**
     * hàm format trạng thái
     * Nguyễn Văn Cương 01/10/2022
     */
    formatStatusRecord(status) {
      //giá trị 1 là hoạt động
      if (status == true) {
        return (status = "Đang sử dụng");
        //giá trị 2 là ngưng hoạt động
      } else if (status == false) {
        return (status = "Ngưng sử dụng");
      } else {
        return (status = "");
      }
    },

    /**
     * hàm format giới tính
     */
    formatGenderRecord(gender) {
      if (gender == true) {
        return "Nam";
      } else return "Nữ";
    },

    formatMonth(month) {
      month = month + " tháng";
      return month;
    },
    FormatVND(price) {
      const suffixes = ["", "nghìn", "triệu", "tỷ", "nghìn tỷ", "triệu tỷ"];
      let suffixIndex = 0;
      let amount = Number(price);
      if (isNaN(amount)) {
        return "0 đồng";
      }
      while (amount >= 1000) {
        amount /= 1000;
        suffixIndex++;
      }
      return `${amount.toFixed(0)} ${suffixes[suffixIndex]} đồng`;
    },
    formatActive(isActive) {
      if (isActive == true) {
        return "Đang hoạt động";
      } else {
        return "Ngừng hoạt động";
      }
    },
    formatComplete(isComplete) {
      if (isComplete == "PENDING") {
        return "Chưa thanh toán";
      } else if (isComplete == "IN_PROGRESS") {
        return "Đang duyệt";
      } else if (isComplete == "COMPLETE") {
        return "Hoàn thành";
      } else {
        return "Đã hủy";
      }
    },
    formatUserCourseStatus(isComplete) {
      if (isComplete == "IN_PROGRESS") {
        return "Đang học";
      } else if (isComplete == "COMPLETE_COURSE") {
        return "Hoàn thành";
      } else if (isComplete == "ALREADY_FEEDBACK") {
        return "Đã đánh giá";
      } else {
        return "Đã hủy";
      }
    },
    formatVerify(isVerify) {
      if (isVerify == true) {
        return "Có";
      } else {
        return "Không";
      }
    },
    formatConsultingtStatus(status) {
      if (status == true) {
        return "Đã xử lí";
      } else {
        return "Chưa xử lí";
      }
    },
  },
  created() {
    this.LoadingShow = true;
    setTimeout(() => {
      this.LoadingShow = false;
    }, 1000);
  },
  watch: {
    RecordsLoad() {
      this.LoadingShow = true;
      setTimeout(() => {
        this.LoadingShow = false;
      }, 1000);
    },
  },
  data() {
    return {
      LoadingShow: false,
      //lưu dữ liệu nhân viên
      records: [],
      //lưu nhân viên đã chọn
      reSelected: {},
      //lưu trạng thái mở popup
      detailFormMode: 1,
      //gọi popup hỏi có xóa không
      isShowAskDelete: false,
      //nút hủy xóa
      popupAskCance: false,
      //lưu id nhân viên cần xóa
      getrecorddetetevalue: 0,
      //lưu code nhân viên cần xóa
      getrecorddetetename: "",
      //lưu trạng thái checkbox
      stateCheckAll: false,
      //lưu danh sách nhân viên cần xóa
      listReSelected: [],
      //hiển thị thông báo
      isShowToast: false,
      //trang thái thông báo
      ToastStatus: true,
      //nội dung thông báo
      ToastMess: {},
      //màu nội dung thông báo
      ToastMess_color: {},
      //css thông báo
      Toastcss: {},
      //icon thông báo
      Toastcssicon: {},
      //lưu vị trí của dropbox
      StyleDropbox: {},
      //thay đổi trạng thái icon mở td con
      Changeicon: false,
      //lưu phát sinh
      IncurredID: null,
      //kiểu mở popup thông báo
      PopupNotificationMode: 2,
      //Tổng số lượng tồn
      SumQuantityV1: null,
      //Tổng giá trị tồn
      SumExistentV1: null,
    };
  },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}

.contentner {
  height: calc(100% - 60px);
  width: 100%;
  overflow: auto;
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
.content-table table {
  width: 100%;
  border-spacing: unset;
  font-size: 13px;
}
.product-tab-th-amount {
  text-align: right !important;
  padding-right: 8px !important;
}

td,
th {
  height: 80px;
  font-size: 13px;
  position: relative;
  text-overflow: ellipsis;
}
.content-table tr td,
.content-table tr th {
  padding-left: 8px;
  text-align: left;
}
.contentner tbody tr:hover {
  cursor: pointer;
  background-color: rgba(80, 184, 60, 0.1);
}
.contentner tr:active {
  background-color: #e5f3ff;
}
.content-table table thead tr th:first-child {
  min-width: 30px;
}

.tab-th-select {
  text-align: center !important;
  padding: 0 !important;
  z-index: 1;
}
.tab-th-amount {
  text-align: right !important;
  z-index: 1;
}
.contentner thead {
  background-color: rgb(236, 238, 241);
}
.contentner table thead tr th:last-child {
  min-width: 110px;

  background-color: rgb(236, 238, 241);
  border-left: 1px solid rgb(216, 211, 211);
}
.contentner table tbody tr td:last-child {
  color: #0075cc;
  justify-content: center;
  font-weight: 600;
  background-color: #fff;
  right: 0;
}
.contentnerf table tbody tr td:last-child {
  color: #0075cc;
  justify-content: center;
  font-weight: 600;
  position: sticky;
  background-color: #fff;
  right: 0;
  z-index: 1;
}
.contentnerf table tbody tr td:last-child:has(.mdrop-datatbl) {
  z-index: 2;
}

.contentnerf table thead tr th:last-child {
  position: sticky;
  right: 0;
  border-left: 1px solid rgb(216, 211, 211);
}
.tab-checkbox {
  width: 100px;
  height: 100px;
  margin-top: 5px;
  margin-left: 3px;
}
.tab-th-select-lable {
  margin-top: 0;
  font-weight: 600;
  cursor: pointer;
}
.btnopendrop {
  position: relative;
  width: 30px;
  height: 30px;
  margin-top: 7px;
}
.content-table table thead tr th {
  font-family: Misa Fonts Bold;
  position: sticky;
  top: 0;
  background-color: rgb(236, 238, 241);
  z-index: 3;
  font-size: 12px;
}
.tooltip {
  width: 140px;
  font-size: 11px;
  position: absolute;
  top: 30px;
  right: 0;
  background-color: #505050;
  border-radius: 2px;
  padding: 2px 4px;
  z-index: 5;
  text-align: center;
  color: #fff;
  visibility: hidden;
}
.tab:hover .tooltip {
  visibility: visible;
  opacity: 1;
  width: 313px;
  font-size: 10px;
  top: 45px;
  right: -70px;
  font-family: Misa Fonts Semibold;
  height: 15px;
}
.tab-b:hover .tooltip {
  visibility: visible;
  opacity: 1;
  font-family: Misa Fonts Semibold;
  width: 150px;
  font-size: 10px;
  right: -5px;
  top: 45px;
  height: 15px;
}
.cmnd:hover .tooltip {
  visibility: visible;
  opacity: 1;
}
.content-table table tfoot {
  background-color: rgb(236, 238, 241);
  height: 30px;
  width: 100%;
  z-index: 4;
  position: sticky;
  bottom: 0;
}
.content-table table tfoot td {
  height: 30px;
  font-family: Misa Fonts Bold;
  border: none;
}
.mutitable {
  display: flex;
  align-items: center;
}
.mutitable-icon {
  background-image: var(--icon);
  background-position: -605px -305px;
  background-repeat: no-repeat;
  width: 30px;
  height: 30px;
}
.mutitable-label {
  padding-left: 50px;
}
.mutitable-icon.b {
  background-position: -556px -305px;
}
.mutitable-label.b {
  padding-left: 28px;
}
.mutitable-label.c {
  padding-left: 0;
}
.filter-header-icon {
  background-image: var(--icon);
  background-position: -1688px -565px;
  background-repeat: no-repeat;
  width: 15px;
  height: 15px;
  float: right;
  margin-right: 5px;
  visibility: hidden;
  cursor: pointer;
}
.content-table table thead tr th:hover .filter-header-icon {
  visibility: visible;
}
.td_img {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  z-index: 99;
  cursor: pointer;
}
</style>
