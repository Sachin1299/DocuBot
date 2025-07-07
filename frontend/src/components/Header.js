import React from 'react';
import { Navbar, Container } from 'react-bootstrap';

export default function Header() {
  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand href="#">🧠 DocuBot</Navbar.Brand>
      </Container>
    </Navbar>
  );
}
