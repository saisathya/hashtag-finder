import React from "react";
import { Box, Button, Text } from "grommet";
import { Close } from "grommet-icons";

const Keyword = ({ keyword, deleteKeywords }) => {
  return (
    <>
      <Box
        direction="row"
        alignContent="center"
        align="center"
        wrap
        round="small"
        pad="xsmall"
        border={{
          size: "xsmall",
          side: "all",
          color: "brand",
        }}
      >
        <Button
          icon={<Close />}
          onClick={() => deleteKeywords(keyword)}
          plain
          size="small"
        />
        <Text color="brand" size="xlarge">
          {keyword}
        </Text>
      </Box>
    </>
  );
};

export default Keyword;
