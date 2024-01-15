<template>
  <div class="combobox-container">
    <div class="cover-combobox">
      <div
        class="droptbl"
        :style="Drstyle"
        v-if="ComboClass === 1"
        :class="{
          'dropbox-input-green': !inValueChange,
          'dropbox-input-green': isShowDropbox,
        }"
      >
        <label for="input" class="text">{{ inputLabel }}</label>
        <input
          class="dropbox-input product"
          type="text"
          :readonly="readonly"
          :style="width"
          :maxlength="maxlength"
          v-model="modelValue"
          @input="handleInput"
          :tabindex="tab"
        />
        <div :class="{ 'dropbox-input add': iconadd }" @click="openPopupEdit">
          <MTooltip
            :content="'Thêm mới'"
            :position="tooltipPosition"
            v-if="iconadd"
          />
        </div>
        <button class="droptbtn" @click="btnDropClick" ref="cover_combobox">
          <div class="droptbtn-icon" :style="style"></div>
        </button>
        <div
          class="dropbl-data"
          v-show="isShowD"
          ref="combobox"
          v-if="!isInfor"
        >
          <div
            class="drop-for"
            v-for="d in DropboxShow"
            :key="d[value]"
            :class="{
              select: d[value] == RecordSle[value],
              select: d[label] == RecordSle[label],
            }"
            @click="selectedRecord(d)"
          >
            <div class="drop-itemtbl">{{ d[label] }}</div>
          </div>
        </div>
        <div
          class="combomuti-data"
          v-show="isShowD"
          :style="width_combomuti"
          ref="combobox"
          v-if="isInfor"
        >
          <div class="combomuti-head">
            <div class="combomuti-head-left">{{ CombolabelLeft }}</div>
            <div class="combomuti-head-right">{{ CombolabelRight }}</div>
          </div>
          <div class="combomuti-cover">
            <div
              class="combomuti-for"
              v-for="d in DropboxShow"
              :key="d[value]"
              :class="{
                select: d[value] == RecordSle[value],
                select: d[label] == RecordSle[label],
              }"
              @click="selectedRecord(d)"
            >
              <div
                class="combomuti-itemtbl"
                @mouseover="onMouseOver($event, d)"
                @mouseleave="onMouseLeave"
              >
                <div class="combomuti-item-left">{{ d[label] }}</div>
                <div class="combomuti-item-center">
                  {{ formatGenderRecord(d[code]) }}
                </div>
                <div class="combomuti-item-right"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="combomuti"
      v-if="ComboClass === 2"
      :class="{
        'dropbox-input-green': !inValueChange,
        'dropbox-input-green': isShowDropbox,
      }"
    >
      <div class="combomuti-input-main">
        <div
          class="combomuti-input-main-data"
          v-for="(item, index) in ComboMutiItem"
          :key="item"
        >
          <div class="combomuti-main-item">
            <div class="item-label">{{ item }}</div>
            <div
              class="item-cancel"
              @click="CancelComboMutiItem(item, index)"
            ></div>
          </div>
        </div>
        <input
          class="dropbox-input product"
          type="text"
          :readonly="readonly"
          :style="width"
          :maxlength="maxlength"
          v-model="modelValue"
          @input="handleInput"
          :tabindex="tab"
        />
      </div>

      <div
        :class="{ 'dropbox-input add': iconadd }"
        @click="openPopupEdit"
      ></div>
      <button class="droptbtn" @click="btnDropClick" ref="cover_combobox">
        <div class="droptbtn-icon" :style="style"></div>
      </button>
      <div
        class="combomuti-data"
        v-show="isShowD"
        :style="width_combomuti"
        ref="combobox"
      >
        <div class="combomuti-head">
          <div class="combomuti-head-left">{{ CombolabelLeft }}</div>
          <div class="combomuti-head-right">{{ CombolabelRight }}</div>
        </div>
        <div class="combomuti-cover">
          <div
            class="combomuti-for"
            v-for="d in DropboxShow"
            :key="d[value]"
            :class="{
              select: d[value] == RecordSle[value],
              select: d[label] == RecordSle[label],
            }"
            @click="selectedMuti(d)"
          >
            <div
              class="combomuti-itemtbl"
              @mouseover="onMouseOver($event, d)"
              @mouseleave="onMouseLeave"
            >
              <div class="combomuti-item-left">{{ d[label] }}</div>
              <div class="combomuti-item-center">
                {{ formatGenderRecord(d[code]) }}
              </div>
              <div class="combomuti-item-right"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <MInfor v-if="openInfor" :Infor="User" />
  </div>
</template>

<script>
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import MInfor from "@/components/MInfor/MInfor.vue";
export default {
  mounted() {
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  unmounted() {
    window.removeEventListener("mouseup", this.clickEventInterrupt);
  },
  components: {
    MTooltip,
    MInfor,
  },
  props: {
    //mảng toàn bộ dữ liệu
    DropboxItem: [],
    tab: Number,
    maxlength: Number,
    //giá trị
    value: String,
    label: String,
    code: String,
    valuePost: String,
    keywordFilter: String,
    //trạng thái search hay không
    readonly: Boolean,
    //thêm được k
    iconadd: Boolean,
    width: String,
    //style tùy chỉnh của drop value
    Drstyle: String,
    //1 object truyền vào
    Comboboxmodel: String,
    ComboboxMutiModel: Array,
    //url để load dữ liệu
    baseURL: String,
    //tên input
    inputLabel: String,
    ComboClass: {
      type: Number,
      default: 1,
    },
    isInfor: {
      type: Boolean,
      default: false,
    },
    CombolabelLeft: String,
    CombolabelRight: String,
    width_combomuti: String,
    //style tùy chỉnh của input
    InputClass: Boolean,
    Reset: Boolean,
    Filter: Boolean,
    inputfilter: Number,
    Tabname: String,
    Defaul: Boolean,
    //trạng thái search hay không
    onSearch: Boolean,
    tooltipPosition: {
      // tooltip position
      type: String,
      required: false,
      default: "right",
    },
    tooltipText: {
      // tooltip text
      type: String,
      required: false,
    },
    BridingMutilCode: String,
    BridingMutilID: String,
    // branch address
    isBranchAddress: Boolean,
    indexOf: Number,
  },
  created() {
    if (this.ComboClass == 2) {
      this.BrindingMuti();
    } else {
      this.BindingModel();
    }
  },
  updated() {
    //hàm mở drop-value sau khi nhập vào input để search
    if (
      this.onSearch == true &&
      this.ValueInput != null &&
      this.readonly == false &&
      this.selectItem == true &&
      this.isShowD == false
    ) {
      this.btnDropClick();
    }
    if (this.DropboxItem) {
      this.DropboxShow = this.DropboxItem;
    }
  },

  methods: {
    BindingModel() {
      //kiểm tra xem object truyền vào có trùng với array load toàn bộ dữ liệu
      try {
        if (this.DropboxItem) {
          this.RecordSle = { ...this.DropboxItem };
          if (this.Comboboxmodel != null) {
            this.DropboxItem.forEach((i) => {
              //nếu có thì hiển thị tên, và chọn
              if (i[this.value] == this.Comboboxmodel) {
                this.RecordSle[this.label] = i[this.label];
                this.modelValue = this.RecordSle[this.label];
                this.$emit("combobox-value", i[this.value]);
              }
            });
          } else {
            if (this.Filter == false) {
              // Xử lý khi Comboboxmodel là null
              // Chọn phần tử đầu tiên trong mảng
              this.RecordSle[this.label] = this.DropboxItem[0][this.label];
              this.modelValue = this.RecordSle[this.label];
              this.$emit("combobox-value", this.DropboxItem[0][this.value]);
            }
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * ẩn infor khi hover ra và set lại giá trị isMouseDown = false để cho phép hiển thị infor khi hover vào
     */
    onMouseLeave() {
      this.openInfor = false;
      this.isMouseDown = false;
      //const infor = this.$refs.infor;
      //infor.style.display = "none";
    },

    /**
     * @description: Hàm này dùng để hiển thị infor khi hover vào
     */
    onMouseOver(event, user) {
      try {
        if (!this.isMouseDown) {
        // nếu isMouseDown = false thì mới cho phép hiển thị
        this.User = user;
        this.openInfor = true;
      }
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * hàm lưu giá trị nhập
     */
    handleInput(event) {
      console.log(1);
      this.selectItem = true;
      this.ValueInput = event.target.value;
      this.$emit("handlSearchCombobox", this.ValueInput);
    },

    /**
     * hàm format giới tính
     */
    formatGenderRecord(gender) {
      if (gender == true) {
        return "Nam";
      } else return "Nữ";
    },

    /**
     * hàm chọn đơn vị
     */
    selectedRecord(drop) {
      try {
        console.log(drop);
      this.isShowDropbox = !this.isShowDropbox;
      this.isShowD = this.isShowDropbox;
      if (this.Filter == true) {
        this.$emit("combobox-value", drop[this.value], this.keywordFilter);
      } else if (this.indexOf) {
        this.$emit("combobox-value", drop[this.value], this.indexOf);
      } else {
        this.$emit("combobox-value", drop[this.value]);
      }
      this.inValueChange = false;
      this.selectItem = false;
      this.RecordSle[this.label] = drop[this.label];
      this.modelValue = this.RecordSle[this.label];
      this.Rotate();
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Không thể chọn");
      }
    },

    /**
    hàm mở combobox
    */
    btnDropClick() {
      //trường hợp combobox dữ liệu có sẵn
      if (this.DropboxItem) {
        this.DropboxShow = this.DropboxItem;
      }
      this.isShowDropbox = !this.isShowDropbox;
      this.isShowD = this.isShowDropbox;
      //xoay icon nút
      this.Rotate();
    },

    /**
    hàm mở popup thêm mới
    */
    openPopupEdit() {
      this.$emit("open-popup-edit");
    },

    /**
    hàm xoay icon nút
    */
    Rotate() {
      if (this.isShowD == true) {
        this.style = "transition: linear 0.2s; transform: rotate(180deg);";
      } else {
        this.style = "transition: linear 0.2s; transform: rotate(0deg);";
      }
    },

    /**
    hàm click outside
    */
    clickEventInterrupt(event) {
      try {
        if (this.isShowD == true) {
          //kiểm tra click có chứa combobox không
          const isClick = this.$refs.combobox.contains(event.target);
          if (!isClick) {
            //nếu không đóng combobox
            this.isShowD = false;
            this.selectItem = false;
            //xoay icon nút
            this.style = "transition: linear 0.2s; transform: rotate(0deg);";
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    /**
        hàm chọn nhiều phần tử
         */
    selectedMuti(muti) {
      try {
        this.inValueChange = false;
      //trường hợp combobox dữ liệu có sẵn
      if (this.DropboxItem) {
        this.DropboxShow = this.DropboxItem;
      }
      this.RecordSle[this.label] = muti[this.label];
      //kiểm tra xem mảng chứa đã có chưa
      if (!this.ComboMutiItem.includes(muti[this.label])) {
        this.ComboMutiItem.push(muti[this.label]);
      }

      if (!this.MutiID.includes(muti[this.value])) {
        this.MutiID.push(muti[this.value]);
      }
      this.$emit("comboboxMuti-value", this.MutiID);
      //xoay icon nút
      this.Rotate();
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Không thể chọn");
      }
    },

    /**
    Hàm hiển thị nhiều tên lên combobox
    */
    BrindingMuti() {
      try {
        if (this.DropboxItem) {
          this.RecordSle = { ...this.DropboxItem };
          if (this.ComboboxMutiModel != null) {
            this.DropboxItem.forEach((i) => {
              this.ComboboxMutiModel.forEach((y) => {
                //nếu có thì hiển thị tên, và chọn
                if (i.id == y.id) {
                  this.RecordSle[this.label] = i[this.label];
                  if (
                    !this.ComboMutiItem.includes(this.RecordSle[this.label])
                  ) {
                    this.ComboMutiItem.push(this.RecordSle[this.label]);
                  }
                  if (!this.MutiID.includes(i.id)) {
                    this.MutiID.push(i.id);
                  }
                  //this.$emit("comboboxMuti-value", this.MutiID);
                }
              });
            });
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    /**
        hàm xóa phần tử trong combobox nhiều
         */
    CancelComboMutiItem(value, pos) {
     try {
        //tìm index của phần từ trong mảng
      const index = this.ComboMutiItem.indexOf(value);
      if (index > -1) {
        //loại bỏ phần tử khỏi mảng
        this.ComboMutiItem.splice(index, 1);
      }
      //tìm index của phần từ trong mảng
      const id = this.MutiID[pos];
      const post = this.MutiID.indexOf(pos);
      if (index > -1) {
        //loại bỏ phần tử khỏi mảng
        this.MutiID.splice(post, 1);
      }

      this.$emit("deletMuti-value", id);
     } catch (error) {
       console.log(error);
     }
    },
  },
  watch: {
    DropboxItem() {
      if (this.isBranchAddress === true) {
        this.modelValue = "";
      }
    },
  },
  data() {
    var RecordSle = {
      //hiện thị giá trị của bảng trên input
      [this.label]: this.Comboboxmodel,
    };
    return {
      //trạng thái đóng mở combobox
      isShowD: false,
      //trạng thái đóng mở combobox
      isShowDropbox: false,
      //lưu giá trị
      selectItem: true,
      //nổi bật đơn vị đã chọn
      inValueChange: true,
      //mảng chứa giá trị combobox
      DropboxShow: [],
      //hiển thị giá trị đã chọn lên input
      RecordSle,
      //lưu giá trị combobox nhiều
      ComboMutiItem: [],
      //lưu vị trí value trong mảng
      indexComboMutiItem: Number,
      //lưu thuộc tính style nút icon
      style: {},
      //lưu trạng thái xóa combobox
      sta: true,
      //lưu lại id khi chọn
      SaveID: null,
      //mảng nhiều phần tử khi chọn
      MutiID: [],
      //lưu giá trị hiển thị
      modelValue: "",
      //lưu giá trị nhập
      ValueInput: null,
      User: {},
      openInfor: false,
    };
  },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.combobox-container {
  width: 100%;
  height: 100%;
}
.select .drop-itemtbl {
  background-color: #2ca01c !important;
  color: #fff !important;
}
.select .combomuti-itemtbl {
  background-color: #2ca01c !important;
  color: #fff !important;
}
.cover-combobox {
  width: 100%;
  height: 100%;
}
.droptbl {
  height: 31px;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  position: relative;
  background-color: #fff;
  margin-top: 5px;
  border-radius: 4px;
  border: 1px solid #bbbbbb;
}

.droptbtn {
  position: absolute;
  border: none;
  right: 1px;
  width: 30px;
  height: 33px;
  flex-shrink: 0;
  flex-basis: 40px;
  cursor: pointer;
  background: #fff;
  z-index: 3;
}
.dropbox-input.product {
  width: 60%;
  height: 27px;
  float: left;
  height: 100%;
  border: none;
  outline: none;
  padding-left: 10px;
  border-radius: 4px;
  cursor: pointer;
}
.dropbox-input.product:focus ~ .droptbl {
  border: 1px solid #50b83c;
  outline: none;
}
.droptbtn-icon {
  background-image: var(--icon);
  background-position: -552px -353px;
  background-repeat: no-repeat;
  width: 30px;
  height: 30px;
}
.droptbtn:hover {
  border: none;
  outline: none;
  background-color: #dddd;
}
.dropbox-input.add:hover {
  background-color: #dddd;
}
.drop-for {
  width: 100%;
}
.dropbl-data {
  position: absolute;
  width: 99%;
  z-index: 1000;
  border: 1px solid #bbbb;
  top: 32px;
  border-radius: 4px;
  overflow-y: scroll;
  overflow-x: hidden;
  max-height: 184px;
}
.dropbox-input.add {
  position: absolute;
  right: 30px;
  background-image: var(--icon);
  background-position: -24px -305px;
  background-repeat: no-repeat;
  width: 32px;
  height: 32px;
  z-index: 5;
  border-right: 1px solid #bbbb;
  cursor: pointer;
}
.drop-itemtbl {
  height: 32px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  padding-left: 8px;
  cursor: pointer;
}
.drop-itemtbl:hover {
  color: #50b83c;
  background-color: #ebedf0;
}
.drop-data-hide {
  display: none;
}
.dropbox-input-green {
  border: 1px solid #50b83c !important;
}
.dropbox-input-red {
  border: 1px solid #ff0000 !important;
}
::-webkit-scrollbar {
  height: 8px;
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 4px;
  cursor: pointer;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #7c869c;
}
.combomuti-data {
  position: absolute;
  width: 100%;
  z-index: 1100;
  border: 1px solid #bbbb;
  top: 38px;
  border-radius: 4px;
  min-height: 80px;
}
.combomuti-cover {
  overflow-y: scroll;
  overflow-x: hidden;
  max-height: 160px;
}
.combomuti-for {
  width: 100%;
}
.combomuti-itemtbl {
  height: 32px;
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 100;
  padding-left: 8px;
  cursor: pointer;
}
.combomuti-itemtbl:hover {
  color: #50b83c;
  background-color: #ebedf0;
}
.combomuti-head {
  display: flex;
  height: 30px;
  font-family: Misa Fonts Bold;
  font-size: 13px;
  padding-top: 10px;
  background-color: #f4f5f8;
  position: relative;
}
.combomuti-head-right {
  position: absolute;
  left: 60%;
}
.combomuti-head-left {
  margin-left: 24px;
}
.combomuti-itemtbl {
  display: flex;
  position: relative;
}
.combomuti-item-center {
  width: 30%;
}
.combomuti-item-left {
  margin-left: 8px;
  width: 50%;
  display: flex;
}
.combomuti-item-right {
  position: absolute;
  right: 0;
  background-image: var(--icon);
  background-position: -1379px -409px;
  background-repeat: no-repeat;
  width: 15px;
  height: 15px;
  margin-right: 15px;
}
.combomuti-input-main {
  width: 74%;
  display: flex;
  flex-wrap: wrap;
}
.combomuti-main-item {
  border: 1px solid #ccc;
  align-items: center;
  border-radius: 3px;
  background-color: #f0f0f0;
  padding: 2px 3px 2px 5px;
  margin-top: 3px;
  margin-left: 4px;
  min-width: 33px;
  overflow: hidden;
  display: flex;
  cursor: pointer;
}
.item-label {
  font-family: Misa Fonts Regular;
  font-size: 11px;
}
.combomuti {
  min-height: 35px;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  position: relative;
  background-color: #fff;
  margin-top: 5px;
  border-radius: 4px;
  border: 1px solid #bbbbbb;
  align-items: center;
}
.combomuti-input {
  min-width: 35%;
  min-height: 27px;
  float: left;
  border: none;
  outline: none;
  padding-left: 10px;
  border-radius: 4px;
  cursor: pointer;
}
.item-cancel {
  background-image: var(--icon);
  background-position: -78px -312px;
  background-repeat: no-repeat;
  width: 15px;
  height: 15px;
}
.text {
  font-size: 0.75rem;
  color: #2ca01c;
  font-weight: 700;
  position: absolute;
  margin: -9px 0 0 7px;
  padding: 0 3px;
  width: fit-content;
  background: #ffffff;
}
</style>
