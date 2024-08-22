// actions.js
export const INCREASE_ADULT = 'INCREASE_ADULT';
export const DECREASE_ADULT = 'DECREASE_ADULT';
export const INCREASE_CHILD = 'INCREASE_CHILD';
export const DECREASE_CHILD = 'DECREASE_CHILD';
export const INCREASE_INFANT = 'INCREASE_INFANT';
export const DECREASE_INFANT = 'DECREASE_INFANT';

export const increaseAdult = () => ({ type: INCREASE_ADULT });
export const decreaseAdult = () => ({ type: DECREASE_ADULT });
export const increaseChild = () => ({ type: INCREASE_CHILD });
export const decreaseChild = () => ({ type: DECREASE_CHILD });
export const increaseInfant = () => ({ type: INCREASE_INFANT });
export const decreaseInfant = () => ({ type: DECREASE_INFANT });
