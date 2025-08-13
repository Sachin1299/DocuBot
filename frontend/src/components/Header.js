import React from 'react';
import { Navbar, Container } from 'react-bootstrap';
import Logout from './Logout';

export default function Header() {
  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand href="/home">🧠 DocuBot</Navbar.Brand>
        <Logout />
      </Container>
    </Navbar>
  );
}

