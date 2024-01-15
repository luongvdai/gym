<template>
    <div class="filter" ref="filter">
      <div class="filter-mid" v-if="isLoading && isLoading === true" >
         <v-skeleton-loader
              v-for="i in 1"
              :key="i"
              class="mx-auto my-3 border"
              max-width="400"
              type="article"
            ></v-skeleton-loader>
      </div>
      <div class="filter-mid" v-else >
          <div class="filter-input12" v-for="index in RecordFilter" :key="index">
            <div class="filter-input1">
              <label class="item-label">{{index.filterlabel}}</label>
                <MCombobox
                  :tab="5"
                  :Drstyle="'height: 58%;'"
                  :DropboxItem="index.model"
                  :maxlength="50"
                  :readonly="index.readonly"
                  :Filter="true"
                  :keywordFilter="index.keyword"
                  :inputfilter="2"
                  @combobox-value="getRecord"
                  @handlSearchCombobox="searchCombobox"
                  :onSearch="isSearching"
                  :value="index.value"
                  :label="index.label"
                  ref="combobox"
                />
            </div>
          </div>
      </div>
      
      <div class="filter-bottom">
        <div class="filter-content-bottom-left">
            <div @click="handleResetCombobox">
                <MButton  :tab="7"  :ButtonCss="'btn-button-cancel'" :text="'Đặt lại'" />
            </div>
          </div>
          <div class="filter-content-bottom-right">
            <div @click="btnFilterClick"><MButton  :tab="6" :ButtonCss="'btn-button-save b'" :text="'Lọc'" />
            </div>  
          </div>
      </div>
    </div>
</template>

<script>
import MButton from '@/components/MButton/MButton.vue';
import MCombobox from "@/components/MCombobox/MCombobox.vue";
export default {
  props:{
    //trạng thái mở loại filter
    FilterMode: Number,
    //style của filter
    FilterStyle: String,
    //trạng thái input của filter
    typeInput: Number,
    //tên cột filter
    Label: String,
    //loại filter cần có
    filterList: Array,
    //object đã search
    searchFilter: Object,
    //tên loại filter đang search
    searchLabel: String,
  },
    components:{
        MButton,
        MCombobox,
    },
    created(){
      if(this.filterList){
        this.RecordFilter = this.filterList;
        this.showLoading();
      }
    },
    updated(){
      //kiểm tra xem object đã search chứa loại filter đang search không
      try {
        if(this.searchFilter && this.searchLabel){
        for (let i = 0; i < this.RecordFilter.length; i++) {
          if(this.RecordFilter[i].filterlabel == this.searchLabel){
            //nếu có thì update model của object đã filter
            this.RecordFilter[i].model = this.searchFilter;
          }
        }
        
      }
      
      } catch (error) {
        console.log(error);
      }
    },

    methods:{

      async showLoading() {
      this.isLoading = true; // Bắt đầu loading

      try {
        // Thực hiện vòng for và các xử lý khác ở đây

        // Ví dụ: chờ 1 giây để mô phỏng việc thực hiện công việc
        await new Promise(resolve => setTimeout(resolve, 1000));

        // Kết thúc loading sau khi hoàn thành công việc
        this.isLoading = false;
      } catch (error) {
        console.error(error);
        // Xử lý lỗi nếu cần
        this.isLoading = false;
      }
    },
      /**
       * Hàm reset lại filter
       */
      handleResetCombobox(){
        this.$emit("get-Filter", null, null);
        this.$emit("start-Filter");
      },

      //Hàm đóng filter
      closeFilter(){
          this.$emit("Close-Filter");
      },
      //Hàm mở filter
      OpenDropFilter(){
        this.isShowDropFilter = !this.isShowDropFilter;
      },
      //Hàm đóng filter 
      CloseFilterDrop(){
        this.isShowDropFilter = false;
      },
      //hàm search
      searchCombobox(value){
        this.$emit("start-Search", value);
        this.isSearching = true;
      },

      /**
       * Hàm lấy các phần từ của record
       */
      getRecord(value, keyword){
        this.$emit("get-Filter", value, keyword);
        
      },
      /***
       * Hàm bắt đầu chạy filter
       */
      btnFilterClick(){
        this.$emit("start-Filter");
      },
    }, 
    beforeRouteLeave(to, from, next) {
    // Destroy trang cũ
    this.destroy();
    next();
  },
  destroy() {
    // Xóa tất cả dữ liệu
    this.RecordFilter = [];
  },
    data(){
      return{
        //trạng thái hiển thị filter
        isShowDropFilter: false,
        //tiêu đề của chọn filter
        FilterLable: "Chứa",
        //lưu style input
        DisableInput: false,
        //trạng thái reset filter
        RecordNull: false,
        //trạng thái chọn loại so sánh
        CompareStatus: false,
        //lưu keyword muốn lọc
        KeywordValue: null,
        //trạng thái search
        isSearching: false,
        RecordFilter: [],
        isLoading: false,
      }
    }
}
</script>

<style scoped>
:root {
  --icon: url("@/assets/icons/Sprites.64af8f61.svg");
}
.filter {
  width: 300px;
  min-height: 230px;
  position: absolute;
  border: 1px solid #bbbb;
  right: 10%;
  top: 25%;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 4px;
  z-index: 1000;
}.filter.b{
  width: 370px;
  height: 210px;
  top: 50%;
  left: 15%;
}
.filter-tooltip{
    width: 70px;
    height: 18px;
    font-size: 12px;
    position: absolute;
    top: 30px;
    right: 0;
    background-color: #505050;
    border-radius: 4px;
    padding: 2px 4px;
    z-index: 5;
    text-align: center;
    color: #fff;
    visibility: hidden;
}.filter-bottom{
    width: 100%;
    height: 70px;  
    border-top: 1px solid #bbbb; 
    display: flex;
    align-items: center;
}.filter-input12{
    margin-left: 20px;
    display: flex;
    margin-top: 10px;
    width: 100%;
    height: 60px;
}.filter-input1{
  width: 85%; 
  position: relative;
}.item-label{
    display: flex;
    font-size: 12px;
    font-weight: bold;
}.filter-content-bottom-left{
  width: 50%;
  height: 50%;
}
.filter-content-bottom-right{
  width: 50%;
  height: 70%;
}
.filter-mid{
  min-height: 150px;
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
</style>


