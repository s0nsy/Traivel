import { configureStore } from "@reduxjs/toolkit";
import chatReducer from "./chatSlice";
import surveyReducer from "./surveySlice";
import buttonReducer from "./buttonSlice";
import selectedItemReducer from "./selectedItemSlice";
import travelReducer from "./travelSlice.js"
import beforeSurveyReducer from "./beforeSurveySlice.js"

const store = configureStore({
  reducer: {
    chat: chatReducer,
    survey: surveyReducer,
    beforeSurvey: beforeSurveyReducer,
    button: buttonReducer,
    selectedItem: selectedItemReducer,
    travel: travelReducer,
  },
});

export default store;
