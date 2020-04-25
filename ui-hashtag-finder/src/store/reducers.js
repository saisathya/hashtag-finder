import C from "../constants";
import { combineReducers } from "redux";

export const keywords = (state = [], { type, payload }) => {
  if (type === C.ADD_KEYWORD) {
    return [...state, payload];
  } else if (type === C.DELETE_KEYWORD) {
    return state.filter((keyword) => keyword !== payload);
  } else {
    return state;
  }
};

export const hashtags = (state = [], { type, payload }) => {
  if (type === C.ADD_HASHTAGS) {
    return payload;
  } else return state;
};

export const fetchHashtags = (state = false, { type, payload }) => {
  if (type === C.FETCH_HASHTAGS) return true;
  else if (type === C.CANCEL_FETCHING) return false;
  else return state;
};

export const recommendedTags = (state = [], { type, payload }) => {
  if (type === C.INIT_SUGGESTED_TAGS) {
    let res = [...state, ...payload];
    return res;
  } else if (type === C.RESET_RECOMMENDED_TAGS) {
    return payload;
  }
  return state;
};

export const userSuggested = (state = [], { type, payload }) => {
  if (type === C.ADD_SUGGESTED_TAGS) {
    return [...state, ...payload];
  } else if (type === C.DELETE_SUGGESTED_TAGS) {
    let difference = state.filter(x => !payload.includes(x));
    return difference;
  } else if (type === C.RESET_SUGGESTED_TAGS) {
    return payload;
  } else return state;
};

export default combineReducers({
  keywords,
  fetchHashtags,
  hashtags,
  suggestedTags: combineReducers({
    recommendedTags,
    userSuggested,
  }),
});

/*
{
  keywords,
  fetchHastags,
  hashtags,
  suggestedTags : {
    recommendedTags,
    userSuggested
  }
}
*/
