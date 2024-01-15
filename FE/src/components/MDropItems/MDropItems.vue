<template>
  <div class="drop" ref="dropbox">
    <div class="mdrop" v-show="MDropSta == 2">
      <button class="mdropbtn" :style="Inputstyle" @click="btnDropClick">
        <label class="mdropbtn-label">{{ mdropbtnlabel }}</label>
        <div class="mdropbtn-button" :style="Inputstyle">
          <div class="droptbtn-icon" :style="style"></div>
        </div>
      </button>
      <div
        class="mdrop-datatbl"
        v-show="OpenDropboxD === true"
        :style="Drstyle"
      >
        <div
          class="drop-for"
          v-for="index in Record"
          :key="index"
          :class="{
            select: index[label] == mdropbtnlabel,
          }"
          @click="SelectedEdit(index.id)"
        >
          <div class="mdrop-itemtbl">{{ index[label] }}</div>
        </div>
      </div>
    </div>

    <div class="mdrop b" v-show="MDropSta == 1">
      <label class="mdropbtn b" :style="Inputstyle" @click="btnDropClick">{{
        labeldrop
      }}</label>
      <div
        class="mdrop-datatbl"
        v-show="OpenDropboxD === true"
        :style="Drstyle"
      >
        <div
          class="mdrop-itemtbl"
          v-for="index in Record"
          :key="index"
          :class="{
            select: index[label] == mdropbtnlabel,
          }"
          @click="SelectedEdit(index.id)"
        >
          {{ index[label] }}
        </div>
      </div>
    </div>
    <div class="mdrop" v-show="MDropSta == 3">
      <button class="mdropbtn" :style="Inputstyle" @click="btnDropClick">
        <img class="mdropimg" :src="urlImage" v-if="urlImage" />
        <img
          class="mdropimg"
          src="@\assets\photos\avatar.png"
          v-if="!urlImage"
        />
        <label class="mdropbtn-label b">{{ mdropbtnlabel }}</label>
        <div class="mdropbtn-button" :style="Inputstyle">
          <div class="droptbtn-icon" :style="style"></div>
        </div>
      </button>
      <div
        class="mdrop-datatbl"
        v-show="OpenDropboxD === true"
        :style="Drstyle"
      >
        <div
          class="drop-for"
          v-for="index in Record"
          :key="index"
          @click="SelectedEdit(index.id)"
          :class="{
            select: index[label] == mdropbtnlabel,
          }"
        >
          <div class="mdrop-itemtbl">
            <img v-if="!index[this.url]" class="mdropimg" src="@\assets\photos\avatar.png" />
            <img v-if="index[this.url]" class="mdropimg" :src="index[this.url]" />
            <label class="mdropbtn-label b">{{ index[label] }}</label>
          </div>
        </div>
      </div>
    </div>
    <div class="mdrop Guest" v-show="MDropSta == 4">
      <div class="mdrop-guest-container">
        <div class="mdrop-guest-icon">
          <i class="ti ti-map-pin"></i>
        </div>
        <div class="mdrop-guest-content" @click="btnDropClick">
          <label class="guest-content-label">Xem tại</label>
          <label class="guest-content-label">{{ mdropbtnlabel }}</label>
        </div>
      </div>
      <div
        class="mdrop-datatbl Guest"
        v-show="OpenDropboxD === true"
        :style="Drstyle"
      >
        <div
          class="drop-for"
          v-for="index in Record"
          :key="index"
          @click="SelectedEdit(index.id)"
          :class="{
            select: index[label] == mdropbtnlabel,
          }"
        >
          <div class="mdrop-itemtbl Guest">
            <img v-if="!index[this.url]" class="mdropimg" src="@\assets\photos\avatar.png" />
            <img v-if="index[this.url]" class="mdropimg" :src="index[this.url]" />
            <label class="mdropbtn-label b">{{ index[label] }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import fireStorage from "@/plugins/fireStorage";
export default {
  mounted() {
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  unmounted() {
    window.removeEventListener("mouseup", this.clickEventInterrupt);
  },
  props: {
    //style của input
    Inputstyle: String,
    //style của dropitem
    Drstyle: String,
    //loại dropitem
    MDropSta: Number,
    //trạng thái mở dropbox không
    OpenDrop: Boolean,
    //Array truyền vào
    RecordDrop: {},
    
    labeldrop: String,
    //1 Object truyền vào
    DropItemsModel: {},
    //giá trị property
    label: String,
    //giá trị property cho trường ảnh
    url: String,
    //vị trí của giá trị truyền vào
    indexOf: Number,
  },
  created() {
    //kiểm tra object truyền vào có trong toàn bộ array không
    try {
      if (this.RecordDrop) {
        this.Record = { ...this.RecordDrop };
        if (this.DropItemsModel != null) {
          this.RecordDrop.forEach((i) => {
            //nếu có hiện name, chọn id
            if (i.id == this.DropItemsModel) {
              this.mdropbtnlabel = i[this.label];
              this.$emit("dropitem-value", i.id);
              //kiểm tra xem nếu dropbox có ảnh k
              if (this.MDropSta == 3) {
                (async () => {
                  //có thì hiện ảnh
                  this.urlImage = await fireStorage.displayImage(i[this.url]);
                })();
              }
            }
          });
        } else {
          this.mdropbtnlabel = this.labeldrop;
          // this.mdropbtnlabel = this.RecordDrop[0][this.label];
          // this.$emit("dropitem-value", this.RecordDrop[0].id);
        }
      }
    } catch (error) {
      console.log(error);
    }
  },
  components: {},
  methods: {
    /**
     * hàm mở dropbox
     */
    btnDropClick() {
      this.OpenDropboxD = !this.OpenDropboxD;
      if (this.RecordDrop) {
        this.Record = { ...this.RecordDrop };
      }
      //xoay icon nút
      this.Rotate();
    },

    /**
     * hàm chọn giá trị trong dropbox
     */
    SelectedEdit(value) {
      try {
        this.RecordDrop.forEach((i) => {
        if (value == i.id) {
          //hiển thị tên, ảnh sau khi chọn lên input
          this.mdropbtnlabel = i[this.label];
          (async () => {
            this.urlImage = await fireStorage.displayImage(i[this.url]);
          })();
        }
      });
      if (this.indexOf) {
        this.$emit("dropitem-value", value, this.indexOf);
      } else {
        this.$emit("dropitem-value", value);
      }

      this.OpenDropboxD = false;
      //xoay icon nút
      this.Rotate();
      } catch (error) {
        console.log(error);
      }
    },
    /**
     * hàm click outsite
     */
    clickEventInterrupt(event) {
      try {
        //lưu vị trí con chuột left, top
        this.PosY = event.y;
        this.PosX = event.x;
        if (this.OpenDropboxD == true) {
          //kiểm tra xem con chuột có click vào dropitem
          const isClick = this.$refs.dropbox.contains(event.target);
          if (!isClick) {
            this.OpenDropboxD = false;
          }
        }
        //xoay icon nút
        this.Rotate();
      } catch (error) {
        console.log(error);
      }
    },
    /**
        hàm xoay icon nút
         */
    Rotate() {
      if (this.OpenDropboxD == true) {
        this.style = "transition: linear 0.2s; transform: rotate(180deg);";
      } else {
        this.style = "transition: linear 0.2s; transform: rotate(0deg);";
      }
    },
  },
  data() {
    return {
      //trạng thái đống mở dropbox
      OpenDropboxD: false,
      //lưu vị trí
      PosY: {},
      //lưu vị trí
      PosX: {},
      //hiển thị name lên input
      mdropbtnlabel: "",
      //array
      Record: {},
      //lưu thuộc tính style nút icon
      style: {},
      //đường dẫn ảnh
      urlImage: null,
    };
  },
};
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.select .mdrop-itemtbl {
  background-color: #2ca01c !important;
  color: #fff !important;
}

.select .mdrop-itemtbl.Guest {
  background-color: #e9485a !important;
  color: #fff !important;
}

.drop {
  width: 100%;
  height: 100%;
}
.mdrop {
  height: 40px;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  position: relative;
}
.mdrop.b {
  display: inline;
}
.mdrop.Guest {
  display: flex;
}
.mdrop-guest-container {
  width: 100%;
  height: 50px;
  display: flex;
  background-color: rgba(0, 0, 0, 0.2);
  color: #fff;
  border-radius: 10px;
}
.mdrop-guest-icon {
  width: 30%;
  height: 100%;
  font-size: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.mdrop-guest-content {
  display: grid;
  cursor: pointer;
}
.guest-content-label {
  padding: 2px 0;
  width: 100px;
  overflow: hidden;
  cursor: pointer;
}

.mdropbtn {
  width: 100%;
  border: 1px solid #bbbbbb;
  margin-top: 5px;
  outline: none;
  padding-left: 10px;
  border-radius: 3px;
  position: absolute;
  min-height: 32px;
  flex-shrink: 0;
  flex-basis: 40px;
  cursor: pointer;
  margin-right: 1px;
  display: flex;
  align-items: center;
}
.mdropbtn.b {
  width: 0;
  margin-left: -8px;
  margin-top: 10px;
  padding: 0;
  border: 0;
  display: inline;
}
.mdropbtn:hover {
  background-color: #dddd;
}
.mdrop-datatbl {
  position: absolute;
  top: 36px;
  width: 120px;
  max-height: 250px;
  background-color: #fff;
  border: 1px solid #bbbb;
  z-index: 1100;
  overflow-x: hidden;
    overflow-y: auto;
    display: block;
}

.mdrop-datatbl.Guest {
  top: 50px;
  left: -20px;
  width: 80px;
}
.mdrop-itemtbl {
  position: relative;
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  padding-left: 10px;
  background-color: #fff;
  font-size: 13px;
  font-weight: lighter;
  color: #000;
  z-index: 1100;
  cursor: pointer;
}

.mdrop-itemtbl:hover {
  color: #50b83c;
  background-color: #ebedf0;
}
.mdrop-data-hide {
  display: none;
}
.mdropbtn-button {
  position: absolute;
  right: 2px;
  font-size: 20px;
  border-left: 1px solid #bbbbbb;
  display: flex;
  align-items: center;
  min-height: 31px;
}
.mdropimg {
  width: 30px;
  height: 30px;
}
.mdropbtn-label {
  white-space: nowrap;
  width: 60%;
  overflow: hidden;
  text-align: center;
}
.mdropbtn-label.b {
  width: 100%;
}
.droptbtn-icon {
  background-image: var(--icon);
  background-position: -552px -353px;
  background-repeat: no-repeat;
  width: 30px;
  height: 30px;
}
.drop-for {
  width: 100%;
}
</style>
