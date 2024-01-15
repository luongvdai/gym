<template>
  <div @click="() => isChecked = !isChecked" :class="{'misa-checkbox':true,'misa-checkbox-checked':isChecked}"  
    >
      <div></div>
  </div>
</template>

<script>
  export default {
      data(){
          var isChecked = this.stateRemember;
          return {
              isChecked,
          }
      },
      props:{
          stateCheckAll:Boolean,
          stateRemember: Boolean,
      },
      //Trạng thái checked của checkbox
      watch:{
          stateCheckAll(newVal,oldVal){
              this.isChecked = newVal;
              console.log(oldVal)
          }
      }
  }
</script>

<style scoped>

@keyframes checked {
    from {
        transform:rotate(-90deg)
    }

    to {
        transform:rotate(0deg)
    }
}
:root{
  --icon: url('@/assets/icons/Sprites.64af8f61.svg');
}
.misa-checkbox{
    width: 18px;
    height: 18px;
    border-radius:4px;
    border: 1px solid #8d9096;
    cursor: pointer;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: center;
    transform:rotate(-90deg);
}

.misa-checkbox-checked {
    border-color:#35bf22;
    transform: rotate(0deg);
    animation:checked .2s ease-in-out;
}
.misa-checkbox-checked div{
	background: var(--icon) no-repeat -1225px -363px;
	width: 14px;
	height: 11px;
}
</style>