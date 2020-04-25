import React from "react";
import { Heading, Header } from "grommet";

const AppHeader = () => {
  return (
    <Header justify="center" fill="horizontal">
      <Heading margin="none" color="brand">
        HashTag Finder
      </Heading>
    </Header>
  );
};

export default AppHeader;
