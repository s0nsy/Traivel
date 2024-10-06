import { createSlice } from '@reduxjs/toolkit';
import moment from 'moment';

const initialState = {
  groupComposition: { 
    adults: 0,
    children: 0,
    infants: 0,
  },
  startDate: null,
  endDate: null,
  schedule: '',
  duration: '',
  purpose: '',
  budget: '',
  keyElement: '',
  accommodation: '',
  transport: '',
  companion: '',
  favorite: '',
  favoriteReason: '',
  specialNeeds: '',
  recommendationType: '',
  freeTime: '',
  importantFactors: '',
  recommendations: [] 
};

const surveySlice = createSlice({
  name: 'survey',
  initialState,
  reducers: {
    setStartDate: (state, action) => {
      state.startDate = action.payload;
      updateScheduleAndDuration(state); 
    },
    setEndDate: (state, action) => {
      state.endDate = action.payload;
      updateScheduleAndDuration(state);  
    },
    setAdults: (state, action) => {
      state.groupComposition.adults = action.payload; // 수정된 부분
    },
    setChildren: (state, action) => {
      state.groupComposition.children = action.payload; // 수정된 부분
    },
    setInfants: (state, action) => {
      state.groupComposition.infants = action.payload; // 수정된 부분
    },
    setPurpose: (state, action) => {
      state.purpose = action.payload;
    },
    setBudget: (state, action) => {
      state.budget = action.payload;
    },
    setkeyElement: (state, action) => {
      state.keyElement = action.payload;
    },
    setAccommodation: (state, action) => {
      state.accommodation = action.payload;
    },
    settransport: (state, action) => {
      state.transport = action.payload;
    },
    setCompanion: (state, action) => {
      state.companion = action.payload;
    },
    setfavorite: (state, action) => {
      state.favorite = action.payload;
    },
    setfavoriteReason: (state, action) => {
      state.favoriteReason = action.payload;
    },
    setspecialNeeds: (state, action) => {
      state.specialNeeds = action.payload;
    },
    setRecommendationType: (state, action) => {
      state.recommendationType = action.payload;
    },
    setfreeTime: (state, action) => {
      state.freeTime = action.payload;
    },
    setimportantFactors: (state, action) => {
      state.importantFactors = action.payload;
    },
    setRecommendations: (state, action) => {
      state.recommendations = action.payload; 
    },
  },
});
const updateScheduleAndDuration = (state) => {
  if (state.startDate && state.endDate) {
    const start = moment(state.startDate);
    const end = moment(state.endDate);
    
    
    state.schedule = `${start.format('M월 D일')} - ${end.format('M월 D일')}`;
    
    
    const nights = end.diff(start, 'days');  
    state.duration = `${nights}박 ${nights + 1}일`;  
  }
};
export const {
  setStartDate,
  setEndDate,
  setAdults,
  setChildren,
  setInfants,
  setPurpose,
  setBudget,
  setkeyElement,
  setAccommodation,
  settransport,
  setCompanion,
  setfavorite,
  setfavoriteReason,
  setspecialNeeds,
  setRecommendationType,
  setfreeTime,
  setimportantFactors,
  setRecommendations, 
} = surveySlice.actions;

export default surveySlice.reducer;
