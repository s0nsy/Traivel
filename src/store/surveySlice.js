import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  travelRequestData: null,
  schedule: '',
  duration: '',
  purpose: '',
  budget: '',
  keyPoint: '',
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
    setSurveyData: (state, action) => {
      state.travelRequestData = action.payload;
    },
    setSchedule: (state, action) => {
      state.setSchedule = action.payload;
    },
    setPurpose: (state, action) => {
      state.purpose = action.payload;
    },
    setBudget: (state, action) => {
      state.budget = action.payload;
    },
    setKeyPoint: (state, action) => {
      state.keyPoint = action.payload;
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
    setspecialNeeds: (state, action) => {
      state.specialNeeds = action.payload;
    },
    setRecommendationType: (state, action) => {
      state.recommendationType = action.payload;
    },
    setfreeTime: (state, action) => {
      state.freeTime = action.payload;
    },
    setRecommendations: (state, action) => {
      state.recommendations = action.payload;
    },
  },
});
export const {
  setSurveyData,
  setSchedule,
  setPurpose,
  setBudget,
  setKeyPoint,
  setAccommodation,
  settransport,
  setCompanion,
  setfavorite,
  setspecialNeeds,
  setRecommendationType,
  setfreeTime,
  setRecommendations,
} = surveySlice.actions;

export default surveySlice.reducer;

