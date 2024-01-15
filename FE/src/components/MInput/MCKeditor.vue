<template>
  <ckeditor
    :editor="editor"
    v-model="internalEditorData"
    :config="editorConfig"
    @ready="onReady"
  />
</template>

<script>
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import { UploadAdapter } from "@/plugins/editorConfig";
import fireStorage from "@/plugins/fireStorage";
export default {
  name: "MCKEditor",
  props: {
    editorData: String,
  },
  watch: {
    internalEditorData: {
      immediate: true,
      handler(newContent, oldContent) {
        this.internalEditorData = newContent;
        const deletedImages = this.findDeletedImages(newContent, oldContent);
        this.deleteImagesFromStorage(deletedImages);
      },
    },
  },

  data() {
    return {
      editor: ClassicEditor,
      editorConfig: {},
      internalEditorData: this.editorData,
    };
  },
  methods: {
    findDeletedImages(newContent, oldContent) {
      const newImages = this.extractImageUrls(newContent);
      const oldImages = this.extractImageUrls(oldContent);
      return oldImages.filter((image) => !newImages.includes(image));
    },
    extractImageUrls(content) {
      const imageRegex = /<img[^>]+src="([^">]+)"/g;
      const matches = content ? content.match(imageRegex) : [];

      if (matches) {
        return matches.map((match) => match.match(/src="([^">]+)"/)[1]);
      }
      return [];
    },
    deleteImagesFromStorage(images) {
      images.forEach((imageUrl) => {
        fireStorage.deleteImage(imageUrl);
      });
    },
    onReady(eventData) {
      eventData.plugins.get("FileRepository").createUploadAdapter = function (
        loader
      ) {
        return new UploadAdapter(loader);
      };
    },
  },
};
</script>

<style>
.ck-content {
  min-height: 150px;
}
</style>
