import{configureStore} from '@reduxjs/toolkit';
import chatReducer from './chatSlice';
import surveyReducer from './surveySlice';

const store = configureStore({
    reducer: {
        chat: chatReducer,
        survey: surveyReducer
    },
})
export default store;