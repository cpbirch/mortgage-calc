/*
 *     Copyright (C) 2016 Christopher Birch
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



var goalId = 2;

// clone the thing
// give it a new ID
// append it
// add the remove option
$("#add-goal-btn").click(function () {
    var groupId = goalId++;
    $(".saving-goal-group")
        .first()
        .clone()
        .appendTo(".spending-goals")
        .find("#savings-goal1")
        .attr("id", "savings-goal" + groupId)
        .val("")
        .focus()
        .parents('.saving-goal-group')
        .first()
        .append('<button type="button" class="btn btn-info btn-minus">-</button>')
        .find('.btn-minus')
        .first()
        .click(function () {
            $("#savings-goal" + groupId).parents(".saving-goal-group").remove()
        });
});

function getMortgage(principal, term, interest, savingsBalance, savingsMonthly) {
    return {
        "principal": parseInt(principal),
        "term_months": parseInt(term),
        "interest_apr": parseFloat(interest / 100),
        "savings_starting_balance": parseInt(savingsBalance),
        "monthly_saving": parseInt(savingsMonthly),
        "saving_goals": []
    };
}

function getSavingGoals(mortgage) {
    $(".form-control-savings-goal").each(function (index) {
        mortgage.saving_goals.push({"saving_goal_description": index + "", "saving_amount": parseInt($(this).val())})
    });
    return mortgage;
}

function enableCalcBtn() {
    $("#calc-btn").click(function () {
        drawChart();
        $(document).scrollTop($("#linechart_material").offset().top);
    });
    $("#calc-btn").prop('disabled', false);
}

function drawChart() {

    var data = new google.visualization.DataTable();

    populateOffsetMortgageData(data);

    var options = {
        chart: {
            title: 'Offset savings repayment mortgage compared with a standard repayment mortgage.',
            subtitle: 'Note that no interest has been added to the savings.'
        },
        width: 900,
        height: 500
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material'));

    chart.draw(data, options);
}

function populateOffsetMortgageData(dataTable) {
    var mortgage = getMortgage(
        $("#principal").val(),
        $("#term").val(),
        $("#interest").val(),
        $("#savings").val(),
        $("#monthlysaving").val()
    );

    getSavingGoals(mortgage);

    alert(JSON.stringify(mortgage));

    dataTable.addColumn('number', 'Months');
    dataTable.addColumn('number', 'Offset Mortgage');
    dataTable.addColumn('number', 'Repayment Mortgage');
    dataTable.addColumn('number', 'Savings');

    $.ajax({
        url: "/api/mortgage/offset",
        contentType: "application/json",
        data: JSON.stringify(mortgage),
//            data: JSON.stringify({
//                "principal": 200400,
//                "term-months": 271,
//                "interest-apr": 0.0275,
//                "savings-starting-balance": 3000,
//                "monthly-saving": 2000,
//                "saving-goals": [{
//                    "saving-goal-description": "HiFi",
//                    "saving-amount": 16000
//                }, {"saving-goal-description": "Kitchen", "saving-amount": 100000}]
//            }),
        dataType: "json",
        method: "post",
        processData: false
    })
        .done(function (data) {
            dataTable.addRows(data);
        })
        .fail(function (data) {
            alert(data);
        });
}
