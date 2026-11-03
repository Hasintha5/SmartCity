<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>City policies - SmartCity360</title>
</head>
<body class="app line-e">
<c:set var="navActive" value="policies"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="app-main">

    <section class="glass flex flex-wrap items-center gap-x-4 gap-y-2 rounded-2xl px-5 py-3">
        <span class="badge-letter">E</span>
        <h1 class="text-xl font-semibold tracking-tight">City policies</h1>
        <span class="pill">Interpreter</span>
        <p class="min-w-[16rem] flex-1 text-sm text-slate-300">
            Policies are written as text. The system reads the text, builds a tree and checks it against the city readings.
        </p>
    </section>

    <div class="fill grid gap-4 lg:grid-cols-[26rem_minmax(0,1fr)]">

        <%-- LEFT: policies, rule and readings --%>
        <div class="glass scroll-y rounded-3xl p-5">

            <p class="field-label">Start from a city policy</p>
            <div class="space-y-2">
                <c:forEach var="p" items="${policies}">
                    <form method="post" action="${pageContext.request.contextPath}/policies">
                        <button type="submit" name="preset" value="${p.key}" class="preset">
                            <span class="tile-title block"><c:out value="${p.title}"/></span>
                            <span class="tile-note block"><c:out value="${p.description}"/></span>
                        </button>
                    </form>
                </c:forEach>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/policies" class="mt-5">
                <label for="rule" class="field-label">Policy rule</label>
                <textarea id="rule" name="rule" rows="4" maxlength="300" required spellcheck="false"
                          class="field field-mono resize-none"><c:out value="${rule}"/></textarea>

                <p class="field-label mt-4">City readings</p>
                <div class="grid grid-cols-2 gap-3">
                    <c:forEach var="v" items="${variables}">
                        <div>
                            <label for="var-${v.key}" class="mb-1 block text-xs text-slate-300">
                                <c:out value="${v.label}"/> (<c:out value="${v.unit}"/>)
                            </label>
                            <input id="var-${v.key}" name="${v.key}" type="number" step="any"
                                   min="${v.min}" max="${v.max}" class="field"
                                   value="<c:out value='${values[v.key]}'/>">
                        </div>
                    </c:forEach>
                </div>

                <button type="submit" class="btn mt-5">Check rule</button>
            </form>
        </div>

        <%-- RIGHT: result --%>
        <div class="flex min-h-0 flex-col gap-4 lg:overflow-y-auto">

            <c:choose>
                <c:when test="${empty evaluation}">
                    <section class="glass shrink-0 rounded-3xl p-5">
                        <h2 class="panel-title">Result</h2>
                        <p class="mt-2 text-sm text-slate-300">Pick a policy or write your own rule, then select Check rule.</p>
                    </section>
                </c:when>

                <c:when test="${not evaluation.valid}">
                    <section class="glass shrink-0 rounded-3xl border-l-4 p-5" style="border-left-color: #fbbf24" role="alert">
                        <h2 class="text-sm font-semibold text-amber-300">The rule could not be read</h2>
                        <p class="mt-2 text-sm"><c:out value="${evaluation.error}"/></p>
                    </section>
                </c:when>

                <c:otherwise>
                    <section class="glass shrink-0 rounded-3xl p-5">
                        <div class="flex flex-wrap items-center justify-between gap-3">
                            <h2 class="panel-title">Result</h2>
                            <span class="status text-sm ${evaluation.compliant ? 'ok' : 'bad'}">${evaluation.compliant ? 'Compliant' : 'Violation'}</span>
                        </div>
                        <p class="mt-3 text-xs text-slate-400">The rule as the system understood it</p>
                        <p class="mt-1 break-words font-mono text-xs text-slate-100"><c:out value="${evaluation.normalized}"/></p>
                    </section>

                    <div class="grid shrink-0 gap-4 xl:grid-cols-2">
                        <section class="glass rounded-3xl p-5">
                            <h2 class="panel-title">Expression tree</h2>
                            <pre class="code mt-3" style="white-space: pre; word-break: normal"><c:out value="${evaluation.treeText}"/></pre>
                        </section>

                        <section class="glass rounded-3xl p-5">
                            <h2 class="panel-title">How it was checked</h2>
                            <ol class="mt-3 space-y-2">
                                <c:forEach var="line" items="${evaluation.trace}" varStatus="st">
                                    <li class="row flex gap-3 px-3 py-2">
                                        <span class="badge-letter !h-6 !w-6 !rounded-full !text-xs"><c:out value="${st.count}"/></span>
                                        <span class="text-xs"><c:out value="${line}"/></span>
                                    </li>
                                </c:forEach>
                            </ol>
                        </section>
                    </div>
                </c:otherwise>
            </c:choose>

            <section class="glass shrink-0 rounded-3xl p-5">
                <h2 class="panel-title">The rule language</h2>
                <div class="mt-3 space-y-2 text-xs text-slate-300">
                    <p><span class="font-semibold text-slate-100">Variables:</span>
                        <c:forEach var="v" items="${variables}" varStatus="st"><code><c:out value="${v.key}"/></code><c:if test="${not st.last}">, </c:if></c:forEach>
                    </p>
                    <p><span class="font-semibold text-slate-100">Comparisons:</span> <code>&lt;</code> <code>&lt;=</code> <code>&gt;</code> <code>&gt;=</code> <code>==</code> <code>!=</code>, followed by a number.</p>
                    <p><span class="font-semibold text-slate-100">Join with:</span> <code>AND</code>, <code>OR</code>, <code>NOT</code> and brackets <code>( )</code>. NOT runs first, then AND, then OR.</p>
                    <p><span class="font-semibold text-slate-100">Example:</span> <code>noise &lt;= 70 AND NOT (hour &gt;= 22)</code></p>
                    <p>A rule that comes out true means the readings follow the policy.</p>
                </div>
            </section>
        </div>
    </div>
</main>
</body>
</html>
