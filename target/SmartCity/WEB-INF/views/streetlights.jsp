<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Street lights - SmartCity360</title>
</head>
<body class="app line-d">
<c:set var="navActive" value="streetlights"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="app-main">

    <section class="glass flex flex-wrap items-center gap-x-4 gap-y-2 rounded-2xl px-5 py-3">
        <span class="badge-letter">D</span>
        <h1 class="text-xl font-semibold tracking-tight">Street lights</h1>
        <span class="pill">Flyweight</span>
        <p class="min-w-[16rem] flex-1 text-sm text-slate-300">
            Thousands of lights share the same settings and differ only in place. The settings are stored once and every light points to them.
        </p>
    </section>

    <div class="fill grid gap-4 lg:grid-cols-[22rem_minmax(0,1fr)]">

        <%-- LEFT: the form --%>
        <form method="post" action="${pageContext.request.contextPath}/streetlights"
              class="glass scroll-y rounded-3xl p-5">

            <p class="field-label">How many street lights?</p>
            <div class="grid grid-cols-2 gap-2.5">
                <c:forEach var="n" items="${counts}">
                    <label class="tile">
                        <input type="radio" name="count" value="${n}" ${count == n ? 'checked' : ''}>
                        <div class="box" style="padding-right: 2.2rem"><p class="tile-title"><fmt:formatNumber value="${n}"/></p></div>
                        <span class="mark" aria-hidden="true"></span>
                    </label>
                </c:forEach>
            </div>

            <label for="hour" class="field-label mt-5">Hour of the day (0 to 23)</label>
            <input id="hour" name="hour" type="number" min="0" max="23" value="${hour}" class="field">
            <p class="mt-2 text-xs text-slate-400">The hour is not stored in any light. It is passed in when a light is asked for its status.</p>

            <button type="submit" class="btn mt-5">Build street lights</button>
        </form>

        <%-- RIGHT: results (two columns on wide screens) --%>
        <div class="grid min-h-0 gap-4 lg:overflow-y-auto xl:grid-cols-2 xl:grid-rows-[minmax(0,1fr)]">

            <div class="flex min-h-0 flex-col gap-4 lg:overflow-y-auto">
                <c:choose>
                    <c:when test="${not empty result}">
                        <section class="glass shrink-0 rounded-3xl p-5">
                            <div class="grid grid-cols-3 gap-3">
                                <div class="stat">
                                    <p class="stat-label">Lights</p>
                                    <p class="stat-value text-xl"><fmt:formatNumber value="${result.count}"/></p>
                                </div>
                                <div class="stat">
                                    <p class="stat-label">Shared types</p>
                                    <p class="stat-value text-xl"><c:out value="${result.created}"/></p>
                                </div>
                                <div class="stat">
                                    <p class="stat-label">Memory saved</p>
                                    <p class="stat-value accent-text text-xl"><c:out value="${result.savedPercentText}"/>%</p>
                                </div>
                            </div>
                            <p class="mt-2 text-xs text-slate-400"><c:out value="${result.created}"/> shared objects were made for <fmt:formatNumber value="${result.requests}"/> requests.</p>

                            <h2 class="panel-title mt-5">Estimated memory</h2>
                            <div class="mt-3 space-y-3 text-sm">
                                <div>
                                    <div class="flex justify-between gap-3 text-slate-300">
                                        <span>Without Flyweight</span><span><c:out value="${result.naiveText}"/></span>
                                    </div>
                                    <div class="mt-1.5 h-2 overflow-hidden rounded-full bg-white/10">
                                        <div class="h-full rounded-full bg-slate-300" style="width: 100%"></div>
                                    </div>
                                </div>
                                <div>
                                    <div class="flex justify-between gap-3 text-slate-300">
                                        <span>With Flyweight</span><span><c:out value="${result.flyweightText}"/></span>
                                    </div>
                                    <div class="mt-1.5 h-2 overflow-hidden rounded-full bg-white/10">
                                        <div class="accent-bg h-full rounded-full"
                                             style="width: ${result.flyweightPercentText}%; min-width: 6px"></div>
                                    </div>
                                </div>
                            </div>
                            <p class="mt-3 text-xs text-slate-400">
                                Time to create the lights: <c:out value="${result.naiveMsText}"/> without, <c:out value="${result.flyweightMsText}"/> with.
                                Timings change from run to run.
                            </p>
                        </section>

                        <section class="glass shrink-0 rounded-3xl p-5">
                            <h2 class="panel-title">First lights, same shared type, different places</h2>
                            <ul class="mt-3 space-y-2">
                                <c:forEach var="line" items="${samples}">
                                    <li class="row px-3 py-2 font-mono text-[11px] leading-snug"><c:out value="${line}"/></li>
                                </c:forEach>
                            </ul>
                        </section>
                    </c:when>
                    <c:otherwise>
                        <section class="glass shrink-0 rounded-3xl p-5">
                            <h2 class="panel-title">Result</h2>
                            <p class="mt-2 text-sm text-slate-300">Choose how many lights to build and select Build street lights. The comparison appears here.</p>
                        </section>
                    </c:otherwise>
                </c:choose>
            </div>

            <section class="glass min-h-0 rounded-3xl p-5 lg:overflow-y-auto">
                <h2 class="panel-title">What happens as the city grows</h2>
                <p class="mt-1 text-xs text-slate-400">Estimates, not measurements.</p>
                <div class="mt-3 overflow-x-auto">
                    <table class="w-full text-left text-xs">
                        <thead class="text-slate-400">
                            <tr>
                                <th class="py-2 pr-3 font-semibold">Lights</th>
                                <th class="py-2 pr-3 font-semibold">Without</th>
                                <th class="py-2 pr-3 font-semibold">With</th>
                                <th class="py-2 font-semibold">Saved</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${scaling}">
                                <tr class="border-t border-white/10">
                                    <td class="py-2 pr-3"><c:out value="${row.countText}"/></td>
                                    <td class="py-2 pr-3"><c:out value="${row.naiveText}"/></td>
                                    <td class="py-2 pr-3"><c:out value="${row.flyweightText}"/></td>
                                    <td class="accent-text py-2 font-semibold"><c:out value="${row.savedPercentText}"/>%</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </div>
</main>
</body>
</html>
