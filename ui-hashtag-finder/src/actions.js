import C from "./constants";
import fetch from "isomorphic-fetch";
import _ from "lodash";

const GET_HASHTAGS_URL = "/hashtagFinder/instagram/getHashtags";
export const addKeyword = (keyword) => ({
  type: C.ADD_KEYWORD,
  payload: keyword,
});

export const deleteKeyword = (keyword) => ({
  type: C.DELETE_KEYWORD,
  payload: keyword,
});

export const addSuggestedTag = (hashtag) => ({
  type: C.ADD_SUGGESTED_TAGS,
  payload: hashtag,
});

export const deleteSuggestedTag = (hashtag) => ({
  type: C.DELETE_SUGGESTED_TAGS,
  payload: hashtag,
});

export const resetSuggestedTags = (hashtag) => ({
  type: C.RESET_SUGGESTED_TAGS,
  payload: hashtag,
});

export const resetRecommendedTags = (hashtag) => ({
  type: C.RESET_RECOMMENDED_TAGS,
  payload: hashtag,
});

export const fetchHashtags = (value) => (dispatch) => {
  dispatch({
    type: C.FETCH_HASHTAGS,
    payload: true,
  });

  dispatch(resetSuggestedTags([]));
  dispatch(resetRecommendedTags([]));

  let body = {
    searchWords: value,
  };
  fetch(GET_HASHTAGS_URL, {
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(body),
  })
    .then((response) => response.json())
    .then((hashtags) => {
      dispatch({
        type: C.ADD_HASHTAGS,
        payload: hashtags,
      });

      let tags = [];
      _.forEach(hashtags, (tag) =>
        _.forEach(tag.hashtags, (hashtag) => tags.push(hashtag))
      );
      _.uniqWith(tags, _.isEqual);
      tags = _.sortBy(tags, (tag) => tag.hashtagPostNumber);
      tags = _.slice(tags, tags.length - 15);
      let recommended = []
      tags = _.forEach(tags, (tag) => recommended.push(tag.hashtagName));
      dispatch({
        type: C.INIT_SUGGESTED_TAGS,
        payload: recommended,
      });

      dispatch(addSuggestedTag(recommended));
    })
    .catch((error) => {
      alert(`error: ${JSON.stringify(error)}`);
    });

  dispatch({
    type: C.CANCEL_FETCHING,
    payload: false,
  });
};

export default () => ({
  addKeyword,
  deleteKeyword,
  fetchHashtags,
});
