import { configureStore } from "@reduxjs/toolkit";
import chatReducer from "./chatSlice";
import surveyReducer from "./surveySlice";
import buttonReducer from "./buttonSlice";
import selectedItemReducer from "./selectedItemSlice";
const store = configureStore({
  reducer: {
    chat: chatReducer,
    survey: surveyReducer,
    button: buttonReducer,
    selectedItem: selectedItemReducer,
  },
});

export default store;
