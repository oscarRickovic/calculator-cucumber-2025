Feature: Notation format for mathematical operations

  Scenario Outline: Verify notation output for operations
    Given I have numbers <value1> and <value2>
    When I perform "<operation>" operation
    Then the notation output should be correctly formatted

    Examples:
      | operation | value1 | value2 |
      | +         | 8      | 6      |
      | -         | 8      | 6      |
      | *         | 8      | 6      |
      | /         | 8      | 6      |