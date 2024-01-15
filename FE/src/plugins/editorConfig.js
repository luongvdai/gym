import fireStorage from "./fireStorage";
class UploadAdapter {
  constructor(loader) {
    this.loader = loader;
  }

  async upload() {
    return new Promise((resolve, reject) => {
      this.loader.file.then(async (file) => {
        try {
          if (!file) return;
          //Call api upload image to server
          // resuls {default: urlImage}
          const name = await fireStorage.getURL("blogs", Date.now(), file);
          const url = await fireStorage.displayImage(name);
          resolve({ default: url });
        } catch (error) {
          reject(error);
        }
      });
    });
  }
}

export { UploadAdapter };
