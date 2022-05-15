/// <reference types="cypress" />

context('Actions', () => {
    beforeEach(() => {
      cy.visit('https://example.cypress.io/commands/actions')
    })
  
  
    it('test7', () => {
      cy.get('.action-email')
        .type('fake@email.com').should('have.value', 'fake@email.com')
  
        .type('{leftarrow}{rightarrow}{uparrow}{downarrow}')
        .type('{del}{selectall}{backspace}')
  
        .type('{alt}{option}') //these are equivalent
        .type('{ctrl}{control}') //these are equivalent
        .type('{meta}{command}{cmd}') //these are equivalent
        .type('{shift}')
  
        .type('slow.typing@email.com', { delay: 100 })
        .should('have.value', 'slow.typing@email.com')
  
      cy.get('.action-disabled')
        .type('disabled error checking', { force: true })
        .should('have.value', 'disabled error checking')
    })
  
    it('test8', () => {
      cy.get('.action-focus').focus()
        .should('have.class', 'focus')
        .prev().should('have.attr', 'style', 'color: orange;')
    })
  
    it('test9', () => {
      cy.get('.action-blur').type('About to blur').blur()
        .should('have.class', 'error')
        .prev().should('have.attr', 'style', 'color: red;')
    })
  
    it('test10', () => {
      cy.get('.action-clear').type('Clear this text')
        .should('have.value', 'Clear this text')
        .clear()
        .should('have.value', '')
    })
  
    it('test11', () => {
      cy.get('.action-form')
        .find('[type="text"]').type('HALFOFF')
  
      cy.get('.action-form').submit()
        .next().should('contain', 'Your form has been submitted!')
    })
  
    it('test12', () => {
      cy.get('.action-btn').click()
  
      // clicking in the center of the element is the default
      cy.get('#action-canvas').click()
  
      cy.get('#action-canvas').click('topLeft')
      cy.get('#action-canvas').click('top')
      cy.get('#action-canvas').click('topRight')
      cy.get('#action-canvas').click('left')
      cy.get('#action-canvas').click('right')
      cy.get('#action-canvas').click('bottomLeft')
      cy.get('#action-canvas').click('bottom')
      cy.get('#action-canvas').click('bottomRight')
      cy.get('#action-canvas')
        .click(80, 75) // click 80px on x coord and 75px on y coord
        .click(170, 75)
        .click(80, 165)
        .click(100, 185)
        .click(125, 190)
        .click(150, 185)
        .click(170, 165)
  
      cy.get('.action-labels>.label').click({ multiple: true })
  
      cy.get('.action-opacity>.btn').click({ force: true })
    })
  
    it('test13', () => {
      cy.get('.action-div').dblclick().should('not.be.visible')
      cy.get('.action-input-hidden').should('be.visible')
    })
  
    it('test14', () => {
      cy.get('.rightclick-action-div').rightclick().should('not.be.visible')
      cy.get('.rightclick-action-input-hidden').should('be.visible')
    })
  
    it('test15', () => {
      cy.get('.action-checkboxes [type="checkbox"]').not('[disabled]')
        .check().should('be.checked')
  
      cy.get('.action-radios [type="radio"]').not('[disabled]')
        .check().should('be.checked')
  
      cy.get('.action-radios [type="radio"]')
        .check('radio1').should('be.checked')
  
      cy.get('.action-multiple-checkboxes [type="checkbox"]')
        .check(['checkbox1', 'checkbox2']).should('be.checked')
  
      cy.get('.action-checkboxes [disabled]')
        .check({ force: true }).should('be.checked')
  
      cy.get('.action-radios [type="radio"]')
        .check('radio3', { force: true }).should('be.checked')
    })
  
    it('test16', () => {
      cy.get('.action-check [type="checkbox"]')
        .not('[disabled]')
        .uncheck().should('not.be.checked')
  
      cy.get('.action-check [type="checkbox"]')
        .check('checkbox1')
        .uncheck('checkbox1').should('not.be.checked')
  
      cy.get('.action-check [type="checkbox"]')
        .check(['checkbox1', 'checkbox3'])
        .uncheck(['checkbox1', 'checkbox3']).should('not.be.checked')
  
      cy.get('.action-check [disabled]')
        .uncheck({ force: true }).should('not.be.checked')
    })
  
    it('test17', () => {
      cy.get('.action-select')
        .should('have.value', '--Select a fruit--')
  
      cy.get('.action-select').select('apples')
      cy.get('.action-select').should('have.value', 'fr-apples')
  
      cy.get('.action-select-multiple')
        .select(['apples', 'oranges', 'bananas'])
        .invoke('val')
        .should('deep.equal', ['fr-apples', 'fr-oranges', 'fr-bananas'])
  
      cy.get('.action-select').select('fr-bananas')
        .should('have.value', 'fr-bananas')
  
      cy.get('.action-select-multiple')
        .select(['fr-apples', 'fr-oranges', 'fr-bananas'])
        .invoke('val')
        .should('deep.equal', ['fr-apples', 'fr-oranges', 'fr-bananas'])
  
      cy.get('.action-select-multiple')
        .invoke('val').should('include', 'fr-oranges')
    })
  
    it('test18', () => {
      cy.get('#scroll-horizontal button')
        .should('not.be.visible')
  
      cy.get('#scroll-horizontal button').scrollIntoView()
        .should('be.visible')
  
      cy.get('#scroll-vertical button')
        .should('not.be.visible')
  
      cy.get('#scroll-vertical button').scrollIntoView()
        .should('be.visible')
  
      cy.get('#scroll-both button')
        .should('not.be.visible')
  
      cy.get('#scroll-both button').scrollIntoView()
        .should('be.visible')
    })
  
    it('test19', () => {
      cy.get('.trigger-input-range')
        .invoke('val', 25)
        .trigger('change')
        .get('input[type=range]').siblings('p')
        .should('have.text', '25')
    })
  
    it('cy.scrollTo() - scroll the window or element to a position', () => {
      cy.scrollTo('bottom')
  
      cy.get('#scrollable-horizontal').scrollTo('right')
  
      // or you can scroll to a specific coordinate:
      // (x axis, y axis) in pixels
      cy.get('#scrollable-vertical').scrollTo(250, 250)
  
      // or you can scroll to a specific percentage
      // of the (width, height) of the element
      cy.get('#scrollable-both').scrollTo('75%', '25%')
  
      // control the easing of the scroll (default is 'swing')
      cy.get('#scrollable-vertical').scrollTo('center', { easing: 'linear' })
  
      // control the duration of the scroll (in ms)
      cy.get('#scrollable-both').scrollTo('center', { duration: 2000 })
    })
  })
  