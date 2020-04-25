import React from "react";
import { Box, Text } from "grommet";
import PropTypes from "prop-types";

const HashtagInfo = ({
  hashtag,
  posts,
  addSuggestedTag,
  deleteSuggestedTag,
  userSuggested,
}) => {
  const isUserSuggested = userSuggested.some((tag) => tag === hashtag);
  const addOrDeleteSuggestedTag = hashtag => {
    if(isUserSuggested)
      deleteSuggestedTag(hashtag);
    else
      addSuggestedTag(hashtag);
  }
  return (
    <>
      <Box
        elevation="small"
        flex="grow"
        background={
          isUserSuggested ? { color: "brand", opacity: "medium" } : "light-1"
        }
        onClick={() => addOrDeleteSuggestedTag(hashtag)}
      >
        <Text margin="xsmall" size="medium">
          {hashtag}
        </Text>
        <Text margin="xsmall" size="xsmall">
          {posts}
        </Text>
      </Box>
    </>
  );
};

HashtagInfo.propTypes = {
  hashtag: PropTypes.string,
  posts: PropTypes.string,
  addSuggestedTag: PropTypes.func,
  deleteSuggestedTag: PropTypes.func,
  resetSuggestedTags: PropTypes.func,
  recommendedTags: PropTypes.array,
  userSuggested: PropTypes.array,
};
export default HashtagInfo;
