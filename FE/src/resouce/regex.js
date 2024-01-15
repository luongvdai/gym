export default{
    //regex kiểm tra email
    RegexEmail: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    
    //regex kiểm tra số điện thoại
    RegexPhone: /^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/,

    RegexUserName: /^[a-zA-Z0-9]+/,

    RegexPassword: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,18}/,

    RegexCode: /[a-zA-Z]{10}/
}