import { configureStore } from '@reduxjs/toolkit';
import chatReducer from './chatSlice';
import surveyReducer from './surveySlice';
import buttonReducer from './buttonSlice';
const store = configureStore({
  reducer: {
    chat: chatReducer,
    survey: surveyReducer,
    button: buttonReducer, 
  },
});

export default store;
