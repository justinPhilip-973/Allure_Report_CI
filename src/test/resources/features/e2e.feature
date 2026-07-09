Feature: Full Happy Journey
  As a shopper
  I want to log in, search products, add them to cart, and place an order
  So that I can complete a purchase
  The retail purchase journey is covered in shared Gherkin Language

  Background:
    Given the retail app is open

    @smoke @login @valid
    Scenario: I Login
      Given I am on the login page
      When I enter valid credentials
      And I click on the login button
      Then I should be redirected to the home page
      And a welcome message should be displayed


    @smoke @addtocart
    Scenario Outline: Buying <product> end to end
      Given I am logged into the retail app
      And I am on home page
      When I click on previewProducts
      Then I am in catalog page
      When I search for "<product>"
      And I add the first result to cart
      Then the cart badge shows <count>
      When I open the cart
      Then the cart has <count> line item
      When I place the order
      Then the order is confirmed

      Examples:
        | product    | count |
        | headphones | 1     |

