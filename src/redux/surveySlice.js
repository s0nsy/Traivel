// features/surveySlice.js
import { createSlice } from '@reduxjs/toolkit';

const surveySlice = createSlice({
  name: 'survey',
  initialState: {
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
    importantFactors:''
  },
  reducers: {
    setPurpose: (state, action) => { state.purpose = action.payload; },
    setBudget: (state, action) => { state.budget = action.payload; },
    setkeyElement: (state, action) => { state.keyElement = action.payload; },
    setAccommodation: (state, action) => { state.accommodation = action.payload; },
    settransport: (state, action) => { state.transport = action.payload; },
    setCompanion: (state, action) => { state.companion = action.payload; },
    setfavorite: (state, action) => { state.favorite = action.payload; },
    setfavoriteReason: (state, action) => { state.favoriteReason = action.payload; },
    setspecialNeeds: (state, action) => { state.specialNeeds = action.payload; },
    setRecommendationType: (state, action) => { state.recommendationType = action.payload; },
    setfreeTime: (state, action) => { state.freeTime = action.payload; },
    setimportantFactors: (state, action) => { state.importantFactors = action.payload; },
    clearSurvey: (state) => {
      state.purpose = '';
      state.budget = '';
      state.keyElement = '';
      state.accommodation = '';
      state.transport = '';
      state.companion = '';
      state.favorite = '';
      state.favoriteReason = '';
      state.specialNeeds = '';
      state.recommendationType = '';
      state.freeTime = '';
      state.importantFactors='';
    },
  },
});

export const {
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
  clearSurvey
} = surveySlice.actions;

export default surveySlice.reducer;
