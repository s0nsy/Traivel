import { createSlice } from "@reduxjs/toolkit";

const initialState = {
   travelRequestData: {},  // /chat에서 입력한 데이터
   selectedItem: {},       // /lists에서 클릭한 여행지
};

const travelSlice = createSlice({
   name: "travel",
   initialState,
   reducers: {
      setTravelRequestData: (state, action) => {
         state.travelRequestData = action.payload;
      },
      setSelectedItem: (state, action) => {
         state.selectedItem = action.payload;
      },
   },
});

export const { setTravelRequestData, setSelectedItem, clearSelectedItem } = travelSlice.actions;
export default travelSlice.reducer;
