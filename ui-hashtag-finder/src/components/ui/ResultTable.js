import React from "react";
import { Box, Text } from "grommet";
import HashtagInfo from "../containers/HashtagInfo";

let toPreetyNumber = (x) => (
    `${x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")} posts`
);

const ResultTable = ({ keyword, hashtags }) => {
  return (
    <>
      <Box gap="xsmall" pad="small" direction="row" align="baseline">
        <Box flex="grow">
          <Text
            margin="xsmall"
            weight="bold"
            textAlign="center"
            alignSelf="center"
          >
            {keyword}
          </Text>
        </Box>
        {hashtags.map((tag, i) => (
          <HashtagInfo
            key={i}
            hashtag={tag.hashtagName}
            posts={toPreetyNumber(tag.hashtagPostNumber)}
          />
        ))}
      </Box>
    </>
  );
};

export default ResultTable;
